var interval = null;
var playbackId = null;
var visualizationCount = 0;
var DRAW_INTERVAL = 100;
var TABLE_INTERVAL = 100;
var drawContext;

play = function(id)
{
	if(!interval)
	{
		var c = document.getElementById("map");
		drawContext = c.getContext("2d");		
		interval = setInterval(requestVisualization, DRAW_INTERVAL);
	}
	playbackId = id;
};

stop = function(id)
{
	if(interval)
	{
		clearInterval(interval);
		interval = null;
	}
	//playbackId = null;
};

requestVisualization = function()
{
	AJAX.sendRequestUrlEncoded(url, "", "get", function(request) {
		matches = JSON.parse(request.response);
		visualize(matches);
	});
};

visualize = function(matches)
{
	var w = document.getElementsByClassName("view")[0].offsetWidth;
	var h = document.getElementsByClassName("view")[0].offsetHeight;
	
	var min = Math.min(w, h) - 20;
	w = min;
	h = min;
	
	drawContext.canvas.width = w;
	drawContext.canvas.height = h;
	
	// select match
	var match = null;
	for(var i = 0; i < matches.length; i++)
	{
		if(matches[i].id == playbackId)
			match = matches[i];
	}
	if(match == null)
	{
		console.log("match not found - stopping");
		stop(playbackId);
	}
	
	var objects = match.map.objects;

	// clear
	drawContext.fillStyle = toColor(match.map.color);
	drawContext.fillRect(0,0,w,h);
	
	var object;
	var pos, start, end;
	for(var i = 0; i < objects.length; i++)
	{
		object = objects[i];
		
		if(object.type == "wall" || object.type == "slider")
		{
			drawContext.strokeStyle = toColor(object.color);
			
			start = toScreenXY(object.start, w, h);
			end = toScreenXY(object.end, w, h);

			drawContext.beginPath();
			drawContext.moveTo(start.x, start.y);
			drawContext.lineTo(end.x, end.y);			
			drawContext.stroke();
			
			console.log(object.type + ": (" + start.x + "," + start.y + ") -> (" + end.x + "," + end.y + ")")
		}
		else if(object.type == "ball")
		{
			drawContext.fillStyle = toColor(object.color);

			pos = toScreenXY(object.position, w, h);
			
			drawContext.beginPath();
			drawContext.arc(pos.x,pos.y,1,0,2*Math.PI);
			drawContext.fill();
		}
	}	
	
	visualizationCount++;
	if(visualizationCount % TABLE_INTERVAL == 0)
		showMatches(matches);
};

toColor = function(rgb)
{
	return "rgb(" + rgb.r + "," + rgb.g + "," + rgb.b + ")";
};

toScreenXY = function(pos, w, h)
{
	return {
		x: (pos.x+1)*w/2,
		y: (pos.y+1)*h/2,
	};
};