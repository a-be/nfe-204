var xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() 
{
    if (this.readyState == 4 && this.status == 200) 
	{
        var myObj = JSON.parse(this.responseText);

		var options = [];
		//options.list = [['foo', 12], ['bar', 6], ['toto', 15], ['moto', 3], ['vélo', 5]];
		options.list = [];
		for(var obj in myObj)
			options.list.push([myObj[obj]["word"], myObj[obj]["occurence"]]);

		options.fontWeight = 600;
		options.backgroundColor = '#eee';
		options.weightFactor = 3;
		options.wait = 150;
		options.hover = 
		function(element, dimension, mousemove)
		{
			if(element != undefined)
			{
				document.getElementById('tooltip').style.top = mousemove.clientY;
				document.getElementById('tooltip').style.left = mousemove.clientX + 15;
				document.getElementById('tooltip').style.display = 'block';
				document.getElementById('tooltip').innerHTML = element[0] + ": " + element[1];
			}
			else
				document.getElementById('tooltip').style.display = 'none';
		};
		
		WordCloud(document.getElementById('cloudCanvas'), options);
	}
};
xmlhttp.open("GET", "https://api.myjson.com/bins/9mypv", true);
xmlhttp.send();