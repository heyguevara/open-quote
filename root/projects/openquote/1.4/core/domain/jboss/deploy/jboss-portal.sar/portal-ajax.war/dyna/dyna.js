function sendData(action, windowId, fromPos, fromRegionId, toPos, toRegionId) {
   var options = {
      requestHeaders: ["ajax","true","bilto","toto"],
      method: "post",
      postBody: "action=" + action + "&windowId=" + windowId + "&fromPos=" + fromPos + "&fromRegion=" + fromRegionId + "&toPos=" + toPos + "&toRegion=" + toRegionId,
      onSuccess: function(t)
      {
      },
      on404: function(t)
      {
         alert("Error 404: location " + t.statusText + " was not found.");
      },
      onFailure: function(t)
      {
         alert("Error " + t.status + " -- " + t.statusText);
      },
      onLoading: function(t)
      {
      }
   }
   new Ajax.Request(server_base_url + "/ajax", options);
}

function snapshot() {

   // Find draggable regions
   var regions_on_page = document.getElementsByClassName("dnd-region");

   // Save current state in the DOM itself
   for(var i = 0;i < regions_on_page.length;i++) {
      var regionDiv = regions_on_page[i]
      for (var j = 0;j < regionDiv.childNodes.length;j++) {
         var child = regionDiv.childNodes[j];
         child["regionId"] = regionDiv.id;
         child["pos"] = j;
       }
   }
}

// Check that the URL starts with the provided prefix
function isURLAccepted(url) {
   if (url.indexOf("http://") == 0)
   {
      var indexOfSlash = url.indexOf("/", "http://".length);
      if (indexOfSlash < 0)
      {
         return false;
      }
      else if (indexOfSlash > 0)
      {
         var path = url.substring(indexOfSlash);
         if (path.indexOf(server_base_url) != 0)
         {
            return false;
         }
      }
   }
   else if (url.indexOf(server_base_url) != 0)
   {
      return false;
   }

   //
   return true;
}

function bilto(event)
{

   // Locate the div container of the window
   var source = Event.element(event);
   var container = Element.up(source, "div.dyna-window");

   // We found the window
   if (container != null) {

      //
      var options = new Object();
      var url;

      //
      if (source.nodeName == "A")
      {

         // Check we can handle this URL
         if (isURLAccepted(source.href)) {

            // Set URL
            url = source.href;

            // We have a get
            options.method = "get"

            // We don't block
            options.asynchronous = false;
         }
      }
      else if (source.nodeName == "INPUT" && source.type == "submit")
      {
         // Find enclosing form
         var current = source.parentNode;
         while (current.nodeName != 'FORM' && current.nodeName != 'BODY') {
            current = current.parentNode;
         }

         // Check we have a form and use it
         if (current.nodeName == 'FORM') {

            var enctype = current.enctype

            // We don't handle file upload for now
            if (enctype != "multipart/form-data") {

               // Check it is a POST
               if (current.method.toLowerCase() == "post") {

                  // Check we can handle this URL
                  if (isURLAccepted(current.action)) {

                     // Set URL
                     url = current.action;

                     // Set the specified enctype
                     options.enctype = enctype;
                     options.asynchronous = false;
                     options.method = "post"
                     options.postBody = Form.serialize(current);
                  }
               }
            }
         }
      }

      // Handle links here
      if (url != null) {

         // Setup headers
         var headers = ["ajax","true"];

         // Add the view state value
         if (view_state != null)
         {
            headers.view_state = view_state;
         }

         // note : we don't convert query string to prototype parameters as in the case
         // of a post, the parameters will be appended to the body of the query which
         // will lead to a non correct request

         // Complete the ajax request options
         options.requestHeaders = headers;
         options.onSuccess = function(t)
         {
            var resp = "";
            eval("resp =" + t.responseText + ";");
            if (resp.type == "update_markup")
            {
               // Iterate all changes
               for (var id in resp.fragments)
               {
                  var matchingElt = document.getElementById(id);

                  // Different than 1 is not good
                  if (matchingElt != null)
                  {
                     var dstContainer = document.getElementById(id);
                     if (dstContainer != null)
                     {
                        // Get markup fragment
                        var markup = resp.fragments[id];

                        // Create a temporary element and paste the innerHTML in it
                        var srcContainer = document.createElement("div");

                        // Insert the markup in the div
                        new Insertion.Bottom(srcContainer, markup);

                        // Copy the region content
                        copyInnerHTML(srcContainer, dstContainer, "dyna-portlet")
                        copyInnerHTML(srcContainer, dstContainer, "dyna-decoration")
                     }
                     else
                     {
                        // Should log that somewhere
                     }
                  }
                  else
                  {
                     // Should log that somewhere
                  }
               }

               // update view state
               if (resp.view_state != null)
               {
                  view_state = resp.view_state;
               }
            }
            else if (resp.type == "update_page")
            {
               document.location = resp.location;
            }
         };

         //
         Event.stop(event);
         new Ajax.Request(url, options);
      }

   }

}

/*
 * Copy the inner content of two zones of the provided containers.
 * The zone are found using the css class names. The operation
 * will succeed only if there is exactly one zone in each container.
 */
function copyInnerHTML(srcContainer, dstContainer, className)
{
   var srcs = Element.getElementsByClassName(srcContainer, className);
   if (srcs.length == 1)
   {
      var src = srcs[0];

      //
      var dsts = Element.getElementsByClassName(dstContainer, className)
      if (dsts.length == 1)
      {
         var dst = dsts[0];

        // Remove existing non attribute children in destination
        var dstChildren = dst.childNodes;
        var copy = new Array();
        for (var i = 0;i < dstChildren.length;i++)
        {
           var dstChild = dstChildren.item(i);
           if (dstChild.nodeType != 2)
           {
              copy[i] = dstChildren.item(i);
           }
        }
        for (var i = 0;i < copy.length;i++)
        {
           Element.remove(copy[i]);
        }

        // Move src non attribute children to the destination
        while (src.hasChildNodes())
        {
           var srcChild = src.firstChild;
           if (srcChild.nodeType != 2)
           {
              dst.appendChild(srcChild);
           }
           else
           {
              src.removeChild(srcChild);
           }
        }
      }
      else
      {
         // Should log that somewhere but
      }
   }
   else
   {
      // Should log that somewhere
   }
}

function footer()
{
   //
   var WindowMoveObserver = Class.create();
   WindowMoveObserver.prototype =
   {
      initialize: function(element)
      {
         this.element = $(element);
      },
      onStart: function()
      {
      },
      onEnd: function()
      {
         var elt = Draggables.activeDraggable.element;

         //
         var windowId = Element.down(elt).id;
         var fromRegionId = elt["regionId"];
         var fromPos = elt["pos"];

         // Doing the snapshot after move will give us the new region and pos of the window
         snapshot();
         var toRegionId = elt["regionId"];
         var toPos = elt["pos"];

         // Perform request
         sendData("windowmove", windowId, fromPos, fromRegionId, toPos, toRegionId);
      }
   };

   // Find the draggable regions
   var regions_on_page = document.getElementsByClassName("dnd-region");

   // Create draggable regions
   for(var i = 0;i < regions_on_page.length;i++) {
      var region = regions_on_page[i];
      Sortable.create(region, {dropOnEmpty:true,handle:"dnd-handle",tag:"div",containment:regions_on_page,constraint:false,hoverclass:"dnd-droppable"});
   }

   //
   Draggables.addObserver(new WindowMoveObserver());

   //
   snapshot();

   // Find the dyna portlets
   var portlets_on_page = document.getElementsByClassName("partial-refresh-window");

   // Add listener for the dyna windows on the dyna-window element
   // and not async-window as this one will have its markup replaced
   for(var i = 0;i < portlets_on_page.length;i++) {
      var portlet = Element.up(portlets_on_page[i]);
      Event.observe(portlet, "click", bilto);
   }
}


//            String u = "" +
//               "function removeWindow(elementId)\n" +
//               "{\n" +
//               "   var effectElement = document.getElementById(elementId)\n" +
//               "   new Effect.BlindUp(effectElement);\n" +
//               "\n" +
//               "   //removeElement(effectElement);\n" +
//               "\n" +
//               "   sendData('windowremove', elementId);\n" +
//               "}\n";
//            markup.append(u);
