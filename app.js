const API_KEY = '11b4457dfd4b69ce8115124acad5a35f';
const BASE = 'https://api.openweathermap.org/data/2.5/weather';

const searchForm = document.getElementById('searchForm');
const cityInput = document.getElementById('cityInput');
const loader = document.getElementById('loader');
const errorEl = document.getElementById('error');
const result = document.getElementById('result');
const weatherAnim = document.getElementById('weatherAnim');

const cityName = document.getElementById('cityName');
const localTime = document.getElementById('localTime');
const weatherIcon = document.getElementById('weatherIcon');
const temp = document.getElementById('temp');
const desc = document.getElementById('desc');
const feels = document.getElementById('feels');
const humidity = document.getElementById('humidity');
const wind = document.getElementById('wind');
const pressure = document.getElementById('pressure');

searchForm.addEventListener('submit', async e => {
  e.preventDefault();
  const q = (cityInput.value||'').trim();
  if(!q) return showError('Please type a city name.');
  clearUI();
  await loadWeather(q);
});

async function doFetch(url){
  try{
    const res = await fetch(url);
    if(!res.ok){
      if(res.status===401) throw new Error('Invalid API key');
      if(res.status===404) throw new Error('City not found');
      throw new Error('Server error');
    }
    return await res.json();
  }catch(err){
    if(err.name==='TypeError') throw new Error('Check your internet connection.');
    throw err;
  }
}

async function fetchByCity(q){
  const url = `${BASE}?q=${encodeURIComponent(q)}&appid=${API_KEY}&units=metric`;
  return await doFetch(url);
}

async function loadWeather(q){
  showLoader(true);
  try{
    const data = await fetchByCity(q);
    render(data);
  }catch(err){ showError(err.message); }
  finally{ showLoader(false); }
}

function render(data){
  cityName.textContent = `${data.name}${data.sys?.country?', '+data.sys.country:''}`;
  const localMs = (data.dt + (data.timezone||0))*1000;
  const d = new Date(localMs);
  localTime.textContent = d.toLocaleString([], {hour:'2-digit', minute:'2-digit', weekday:'short', day:'numeric', month:'short'});

  desc.textContent = capitalize(data.weather[0].description||'');
  temp.textContent = `${Math.round(data.main.temp)}°C`;
  temp.style.color = tempColor(Math.round(data.main.temp));
  feels.textContent = `${Math.round(data.main.feels_like)}°C`;
  humidity.textContent = `${data.main.humidity}%`;
  wind.textContent = `${data.wind.speed} m/s`;
  pressure.textContent = `${data.main.pressure} hPa`;

  const iconCode = data.weather[0].icon;
  weatherIcon.src = `https://openweathermap.org/img/wn/${iconCode}@2x.png`;
  weatherIcon.alt = data.weather[0].description||'';

  // Clear previous animations
  weatherAnim.innerHTML=''; document.body.className='';

  const main = (data.weather[0].main||'').toLowerCase();
  if(main.includes('clear')){ document.body.classList.add('clear'); addClouds(3);}
  else if(main.includes('cloud')){ document.body.classList.add('clouds'); addClouds(5);}
  else if(main.includes('rain')){ document.body.classList.add('rain'); addRain(100);}
  else if(main.includes('drizzle')){ document.body.classList.add('drizzle'); addRain(80);}
  else if(main.includes('thunderstorm')){ document.body.classList.add('thunderstorm'); addRain(120);}
  else if(main.includes('snow')){ document.body.classList.add('snow'); addSnow(60);}
  else{ document.body.classList.add('clouds'); addClouds(4); }

  errorEl.classList.add('hidden');
  result.classList.add('show');
  cityInput.value='';
}

// Animations
function addRain(count=100){
  for(let i=0;i<count;i++){
    const drop=document.createElement('div');
    drop.style.left=Math.random()*100+'%';
    drop.style.animationDuration=(0.5+Math.random()*0.5)+'s';
    drop.style.height=(5+Math.random()*15)+'px';
    drop.style.opacity=Math.random();
    weatherAnim.appendChild(drop);
  }
}

function addSnow(count=60){
  for(let i=0;i<count;i++){
    const snow=document.createElement('div');
    snow.textContent='❄';
    snow.style.left=Math.random()*100+'%';
    snow.style.fontSize=(10+Math.random()*20)+'px';
    snow.style.animationDuration=(5+Math.random()*5)+'s';
    snow.style.opacity=Math.random();
    weatherAnim.appendChild(snow);
  }
}

function addClouds(count=5){
  for(let i=0;i<count;i++){
    const cloud=document.createElement('div');
    cloud.classList.add('cloud');
    cloud.style.width=(80+Math.random()*100)+'px';
    cloud.style.height=(40+Math.random()*50)+'px';
    cloud.style.top=(Math.random()*40)+'%';
    cloud.style.left=(Math.random()*100)+'%';
    cloud.style.animationDuration=(20+Math.random()*20)+'s';
    weatherAnim.appendChild(cloud);
  }
}

// Temperature color gradient
function tempColor(t){
  if(t<=0) return '#60a5fa';       // cold blue
  if(t>0 && t<=15) return '#3b82f6'; // cool blue
  if(t>15 && t<=25) return '#facc15'; // yellow
  if(t>25) return '#f87171';        // red hot
}

// Helpers
function showLoader(show){ loader.classList.toggle('hidden',!show);}
function showError(msg){ errorEl.textContent=msg; errorEl.classList.remove('hidden'); result.classList.remove('show'); }
function clearUI(){ errorEl.classList.add('hidden'); errorEl.textContent=''; result.classList.remove('show'); }
function capitalize(s){ return s?s.charAt(0).toUpperCase()+s.slice(1):'';}