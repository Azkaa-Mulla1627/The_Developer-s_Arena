const taskInput = document.getElementById('taskInput');
const addTaskBtn = document.getElementById('addTaskBtn');
const taskTableBody = document.querySelector('#taskTable tbody');

// Load tasks from localStorage on page load
window.onload = function() {
    const tasks = JSON.parse(localStorage.getItem('tasks')) || [];
    tasks.forEach(task => addTaskToTable(task.text, task.completed));
}

// Add task
addTaskBtn.addEventListener('click', () => {
    const taskText = taskInput.value.trim();
    if(taskText) {
        addTaskToTable(taskText);
        saveTask(taskText);
        taskInput.value = '';
    } else {
        alert('Please enter a task!');
    }
});

// Add row to table
function addTaskToTable(taskText, completed=false) {
    const tr = document.createElement('tr');

    // Task cell
    const taskCell = document.createElement('td');
    taskCell.textContent = taskText;
    if(completed) taskCell.classList.add('completed');
    tr.appendChild(taskCell);

    // Status cell
    const statusCell = document.createElement('td');
    statusCell.textContent = completed ? "Completed" : "Pending";
    tr.appendChild(statusCell);

    // Actions cell
    const actionsCell = document.createElement('td');

    // Complete button
    const completeBtn = document.createElement('button');
    completeBtn.textContent = 'Complete';
    completeBtn.classList.add('action-btn', 'complete-btn');
    completeBtn.addEventListener('click', () => {
        taskCell.classList.toggle('completed');
        statusCell.textContent = taskCell.classList.contains('completed') ? "Completed" : "Pending";
        updateLocalStorage();
    });

    // Delete button
    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = 'Delete';
    deleteBtn.classList.add('action-btn', 'delete-btn');
    deleteBtn.addEventListener('click', () => {
        tr.remove();
        updateLocalStorage();
    });

    actionsCell.appendChild(completeBtn);
    actionsCell.appendChild(deleteBtn);
    tr.appendChild(actionsCell);

    taskTableBody.appendChild(tr);
}

// Save task to localStorage
function saveTask(taskText) {
    const tasks = JSON.parse(localStorage.getItem('tasks')) || [];
    tasks.push({text: taskText, completed: false});
    localStorage.setItem('tasks', JSON.stringify(tasks));
}

// Update localStorage after actions
function updateLocalStorage() {
    const tasks = [];
    document.querySelectorAll('#taskTable tbody tr').forEach(tr => {
        const text = tr.cells[0].textContent;
        const completed = tr.cells[0].classList.contains('completed');
        tasks.push({text, completed});
    });
    localStorage.setItem('tasks', JSON.stringify(tasks));
}
