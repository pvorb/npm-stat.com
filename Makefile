all:
	browserify src/charts.js > public/charts.js

release:
	browserify src/charts.js | uglifyjs > public/charts.js
