	function triArea(mulNum, range) {
		document.write("<table border='1px'>");
		document.write("<thead>");
		document.write("<tr><th>A * B</th>");
		document.write("<th>Result</th></tr>");
		document.write("</thead>");
		document.write("<tbody>");
		var area;
		for (var i = 0; i <= range; i++) {
			document.write("<tr><td>" + i + " * " + mulNum + " = </td>");
			document.write("<td>" + (i * mulNum) + "</td>");
			document.write("</tr>");
		}
		document.write("</tbody></table>");
	}
