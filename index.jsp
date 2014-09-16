<!--
 Name: William Jeremy RiCharde
 Course: CNT 4714 - Summer 2014 - Project 4
 Assignment title: Developing A Three-Tier Disributed Web-Based Application
 Date: July 11, 2014
 
 index to be main webpage displayed in browser
 -->



<?xml version = "1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<!-- index.jsp -->

<html xmlns = "http://www.w3.org/1999/xhtml">

   <head>
      <title>Project 4</title>

      <style type = "text/css">
         body 
         { 
            font-family: tahoma, helvetica, arial, sans-serif; 
         }

         table, tr, td 
         { 
            font-size: 1.1em;
            border: 3px groove;
            padding: 5px;
            background-color: lightblue; 
         }
      </style>
   </head>

   <body >
      <table>
         <tr bgcolor = blue>
            <td>
               <jsp:include page = "top.html" 
                  flush = "true" />
            </td>
         </tr>
         <tr>
            <td style = "width: 250px">
               <jsp:include page = "query.html" 
                  flush = "true" />
            </td>
            
         </tr>
      </table>
   </body>
</html>


