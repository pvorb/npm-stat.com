!function(t){function e(a){if(n[a])return n[a].exports;var r=n[a]={exports:{},id:a,loaded:!1};return t[a].call(r.exports,r,r.exports,e),r.loaded=!0,r.exports}var n={};return e.m=t,e.c=n,e.p="",e(0)}([function(t,e,n){function a(t,e){for(var n=[],a=moment(t).startOf("day"),r=moment(e).startOf("day");a.isSameOrBefore(r);)n.push(a.toDate()),a=a.add(1,"days");return n}function r(t,e,n,a,r,o){var i=$.map(n,function(t,e){return{name:e,data:t,type:"spline"}});new Highcharts.Chart({chart:{renderTo:t,zoomType:"x"},colors:["#CC333F","#00A0B0","#73AC42","#EB6841","#542437"],title:{text:e,style:{color:"#000000"}},subtitle:{text:"undefined"==typeof document.ontouchstart?"Click and drag in the plot to zoom in":"Drag your finger over the plot to zoom in",style:{color:"#000000"}},exporting:{enableImages:!0},credits:{enabled:!1},xAxis:"datetime"==a?{type:a,maxZoom:12096e5,lineColor:"#000000",title:{text:r,style:{color:"#000000"}}}:{type:a,lineColor:"#000000",categories:o,labels:{rotation:-20},title:{text:r,style:{color:"#000000"}}},yAxis:{min:0,startOnTick:!1,showFirstLabel:!1,title:{text:"Downloads",style:{color:"#000000"}}},tooltip:{shared:!0},legend:{enabled:i.length>1},plotOptions:{spline:{marker:{radius:1.5},lineWidth:1,states:{hover:{lineWidth:1}},threshold:null}},series:i})}function o(t){var e=0;return $.each(t,function(t,n){e+=n}),e}function i(t){return t.toString().replace(/\B(?=(\d{3})+(?!\d))/g,",")}function l(t){var e={},n=t.downloads,a=null;if(n)for(var r=0;r<n.length;r++)a=n[r].day,e[a]=n[r].downloads;return e}function c(t,e){var n={};return $.each(t,function(t,a){for(var r=[],o=0;o<e.length;o++){var i=s(e[o]),l=moment(e[o]).startOf("day").valueOf();r.push([l,a[i]||0])}n[t]=r}),n}function s(t){return moment(t).format("YYYY-MM-DD")}function u(t){return moment(t).format("GGGG-[W]WW")}function p(t){return moment(t).format("MMM YYYY")}function d(t){return moment(t).format("YYYY")}function f(t,e,n,a){var r={},o=null,i={};$.each(t,function(t,a){for(var l=[],c=0,u=null,p=0;p<e.length;p++){var d=s(e[p]),f=n(e[p]);null!==i&&(i[f]=!0),f!==u&&null!==u&&(l.push({name:u,y:c}),c=0),c+=a[d]||0,u=f,p===e.length-1&&l.push({name:f,y:c})}null!==i&&(o=Object.keys(i)),i=null,r[t]=l});for(var l=0;l<o.length;l++)l%a!=0&&(o[l]=" ");return{data:r,xAxisLabels:o}}function m(t){for(var e=[],n=t.rows.length,a=0;a<n;a++)e.push(t.rows[a].key[1]);return e}function h(t){return $.ajax({url:t,dataType:"json"})}function g(t,e,n){return"/downloads/range/"+s(e)+":"+s(n)+"/"+encodeURIComponent(t)}function v(t){var e={};return $.each(t,function(t,n){$.each(n,function(t,n){"undefined"!=typeof e[t]?e[t]=e[t]+n:e[t]=n})}),{total:e}}function y(t,e,n){var o=a(e,n),i=c(t,o);r("days","Downloads per day",i,"datetime","Date");var l=f(t,o,u,4);r("weeks","Downloads per week",l.data,"categories","Week",l.xAxisLabels);var s=f(t,o,p,1);r("months","Downloads per month",s.data,"categories","Month",s.xAxisLabels);var m=f(t,o,d,1);r("years","Downloads per year",m.data,"categories","Year",m.xAxisLabels)}function w(t,e,n,a){var r=0,l=$('<table class="alternating"><tr><td>package</td><td>downloads</td></tr></table>'),c=$.map(t,function(t,e){var n=o(t);return a&&(r+=n),{packageName:e,downloads:n}}),u=c.sort(function(t,e){return e.downloads-t.downloads});$.each(u,function(t,r){var o;o=a?'<a href="charts.html?package='+r.packageName+"&from="+s(e)+"&to="+s(n)+'">'+r.packageName+"</a>":r.packageName,l.append("<tr><td>"+o+"</td><td>"+i(r.downloads)+"</td></tr>")}),a&&l.append("<tr><td><strong>Σ</strong></td><td><strong>"+i(r)+"</strong></td></tr>"),$("#pkgs").after(l).after("<p>Total number of downloads between <em>"+s(e)+"</em> and <em>"+s(n)+"</em>:")}function k(t,e,n){var a=t.join(", ");if($("h2").append(" for package"+(t.length>1?"s":"")+" <em>"+a+"</em>"),A.val("package"),$("#name").attr("name","package").val(a),t.length>5)return void window.alert("You cannot compare more than 5 packages at once.");var r=$("#npm-stat");r.after('<p id="loading"><img src="loading.gif" /></p>'),1==t.length&&r.after('<p><a href="https://npmjs.org/package/'+t+'">View package on npm</a></p>');var o={},i=[];$.each(t,function(t,a){var r=g(a,e,n),l=h(r);o[a]=l,i.push(l)}),$.when.apply(this,i).then(function(){var a={};1==t.length?a[t[0]]=arguments[0]:$.each(arguments,function(e,n){a[t[e]]=n[0]});var r={};$.each(a,function(t,e){r[t]=l(e)}),$("#loading").remove(),w(r,e,n,!1),y(r,e,n)})}function b(t,e,n){$("h2").html("Downloads for author <em>"+t+"</em>"),A.val("author"),$("#name").attr("name","author").val(t),$("#npm-stat").after('<p id="loading"></p><p><a href="https://npmjs.org/~'+t+'">View author on npm</a></p>'),$("#loading").html('<img src="loading.gif" />'),x(t).then(function(t){var a=m(t),r={},o=[];$.each(a,function(t,a){var i=g(a,e,n),l=h(i);r[a]=l,o.push(l)}),$.when.apply(this,o).then(function(){var t={};1==a.length?t[a[0]]=arguments[0]:$.each(arguments,function(e,n){t[a[e]]=n[0]});var r={};$.each(t,function(t,e){r[t]=l(e)}),$("#loading").remove(),w(r,e,n,!0);var o=v(r);y(o,e,n)})})}function x(t){var e='/-/_view/browseAuthors?group_level=3&start_key=["'+t+'"]&end_key=["'+t+'",{}]';return h(e)}function O(t,e,n){var a;return a=t[e]?moment(t[e]).startOf("day"):n?moment(n).startOf("day").subtract(1,"year"):moment().startOf("day"),a.isValid()?(a=a.toDate(),$('input[name="'+e+'"]').val(s(a)),a):void alert('Invalid date format in URL parameter "'+e+'"')}/*!
	 * (c) 2012-2016 Paul Vorbach.
	 *
	 * MIT License (https://vorba.ch/license/mit.html)
	 */
var j=n(3);n(4);var A=$('<select id="nameType">\n    <option value="package" selected>Package</option>\n    <option value="author">Author</option>\n</select>');A.change(function(){"package"==A.val()?$("#name").attr("name","package").attr("placeholder","package name(s) (comma separated)"):"author"==A.val()&&$("#name").attr("name","author").attr("placeholder","author name")}),$(function(){$("#nameType").replaceWith(A),$("#from, #to").attr("title","If the date fields are omitted, you will see the data of the past year.");var t=j.decode(window.location.search?window.location.search.substring(1):""),e=t["package"],n=escape(t.author);if(e||n){var a=O(t,"to"),r=O(t,"from",a);e?(e instanceof Array||(e=[e]),e=$.map(e,function(t){return $(t.trim()).text()}),$("title").html("npm-stat: "+e.join(", ")),k(e,r,a)):n&&($("title").html("npm-stat: "+n),b(n,r,a))}}),window.submitForm=function(){var t={};if("package"==A.val()){var e=$("input[name=package]").val().split(",");e.length>=1&&""!==e[0].trim()?t["package"]=$.map(e,function(t){return t.trim()}):t["package"]=["clone"]}else if("author"==A.val()){var n=$("input[name=author]").val();t.author=n||"pvorb"}t.from=$("#from").val(),t.to=$("#to").val();for(var a in t)t[a]||delete t[a];return window.location="/charts.html?"+j.encode(t),!1}},function(t,e){"use strict";function n(t,e){return Object.prototype.hasOwnProperty.call(t,e)}t.exports=function(t,e,a,r){e=e||"&",a=a||"=";var o={};if("string"!=typeof t||0===t.length)return o;var i=/\+/g;t=t.split(e);var l=1e3;r&&"number"==typeof r.maxKeys&&(l=r.maxKeys);var c=t.length;l>0&&c>l&&(c=l);for(var s=0;s<c;++s){var u,p,d,f,m=t[s].replace(i,"%20"),h=m.indexOf(a);h>=0?(u=m.substr(0,h),p=m.substr(h+1)):(u=m,p=""),d=decodeURIComponent(u),f=decodeURIComponent(p),n(o,d)?Array.isArray(o[d])?o[d].push(f):o[d]=[o[d],f]:o[d]=f}return o}},function(t,e){"use strict";var n=function(t){switch(typeof t){case"string":return t;case"boolean":return t?"true":"false";case"number":return isFinite(t)?t:"";default:return""}};t.exports=function(t,e,a,r){return e=e||"&",a=a||"=",null===t&&(t=void 0),"object"==typeof t?Object.keys(t).map(function(r){var o=encodeURIComponent(n(r))+a;return Array.isArray(t[r])?t[r].map(function(t){return o+encodeURIComponent(n(t))}).join(e):o+encodeURIComponent(n(t[r]))}).join(e):r?encodeURIComponent(n(r))+a+encodeURIComponent(n(t)):""}},function(t,e,n){"use strict";e.decode=e.parse=n(1),e.encode=e.stringify=n(2)},function(t,e){Object.keys||(Object.keys=function(){"use strict";var t=Object.prototype.hasOwnProperty,e=!{toString:null}.propertyIsEnumerable("toString"),n=["toString","toLocaleString","valueOf","hasOwnProperty","isPrototypeOf","propertyIsEnumerable","constructor"],a=n.length;return function(r){if("object"!=typeof r&&("function"!=typeof r||null===r))throw new TypeError("Object.keys called on non-object");var o,i,l=[];for(o in r)t.call(r,o)&&l.push(o);if(e)for(i=0;i<a;i++)t.call(r,n[i])&&l.push(n[i]);return l}}())}]);
//# sourceMappingURL=charts.js.map