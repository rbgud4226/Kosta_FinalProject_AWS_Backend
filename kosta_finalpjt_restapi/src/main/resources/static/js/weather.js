function getLocation() {
	navigator.geolocation.getCurrentPosition(showLocation);
}

function showLocation(position) {
	const lat = position.coords.latitude;
	const lon = position.coords.longitude;
	showWeather(lat, lon);
}

function showWeather(lat, lon) {
	fetch(`/auth/getWeather?lat=${lat}&lon=${lon}`)
		.then(response => response.json())
		.then(data => {
			let weatherInfo = '';
			weatherInfo += '<table class="weather-table">';
			weatherInfo += '<tr>';
			data.forEach(item => {
				let dateTime = item.dateTime.replace(" 00시", "");
				weatherInfo += `<td>${dateTime}</td>`;
			});
			weatherInfo += '</tr><tr>';
			data.forEach(item => {
				weatherInfo += `<td><img src="https://openweathermap.org/img/wn/${item.icon}@2x.png" alt="Weather icon"></td>`;
			});
			weatherInfo += '</tr><tr>';
			data.forEach(item => {
				weatherInfo += `<td>${item.temp}°C</td>`;
			});
			weatherInfo += '</tr></table>';
			document.getElementById("weather").innerHTML = weatherInfo;
		})
}

$(document).ready(function() {
	getLocation();
});