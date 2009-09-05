/* 'item' is a string, 'list' is a string of semicolon separated values. This
 * function returns true if 'item' appears in 'list'.
 */
function isInList(item, list) {
	return (item==list) || 
		   (list.indexOf(item+";")==0) ||
		   (list.indexOf(";"+item)==(list.length-(item.length+1))) ||
		   (list.indexOf(";"+item+";")>0); 
}

/* IE's implementation of getElementsByName is very patchy - works in some 
 * versions and not in others. Hence this local implementation that will work
 * on all versions of IE and other browsers.
 */
function findElementsByName(name) {
  var tags = new Array();
  var ret = new Array();
  var retIdx;
  
  tags[0]="select"; tags[1]="input"; tags[2]="textarea";

  for(retIdx=0, t=0 ; t<tags.length ; t++) {
    var elem=document.getElementsByTagName(tags[t]);
    
    for(i=0 ; i<elem.length ; i++) {
      if (elem[i].getAttribute("name") == name) {
        ret[retIdx++]=elem[i];
      }
    }
  }
  
  return ret;
}

/* If 'condition' is true then disable the 'target' page element, 
 * otherwise enable it.
 */
function disableTargetIf(condition, targetName) {
    findElementsByName(targetName)[0].disabled=condition;
}

/* If 'condition' is true then enable the 'target' page element, 
 * otherwise disable it.
 */
function enableTargetIf(condition, targetName) {
    disableTargetIf(!condition, targetName);
}

/* Expand and show a hidden div section.
 */
function showDivDisplay(id)
{
   var mydiv = document.getElementById(id);
   mydiv.style.visibility="";
   mydiv.style.display="";
}

/* Shrink and hide a div section.
 */
function hideDivDisplay(id)
{
   var mydiv = document.getElementById(id);
   mydiv.style.visibility="";
   mydiv.style.display="none";
}

function showHideDivDisplay(showCondition, hideCondition, id) {
    if (showCondition) {
       showDivDisplay(id);
    }
    else if (hideCondition) {
       hideDivDisplay(id);
    }
}

function _addOption(selectbox, text, selected) {
    var optn = document.createElement("OPTION");
    optn.text = text;
    optn.selected=selected;
    // Below: "selectbox.options.add" would be nicer - but safari doesn't like it.
    selectbox.options[selectbox.length]=optn;
}

/* This method is called to load options into a choice (drop down menu). It'll be called once
 * for each row (in a rowscroller etc), it is also used to populate the 'Master' menu in a 
 * master/slave setup.
 */
function loadChoiceOptions(select, selected, array) {
    select.options.length=0;
    for (var i=1 ; i < array.length ; i++) {
        _addOption(select, array[i][0], selected==array[i][0]);
    }
}

/* On page load, load the model options appropriate to whatever make is currently
 * selected.
 */
function loadSlaveChoiceOptions(select, selected, array, master, slave) {
    masterSelectName=new String(select.name);
    masterSelectName=masterSelectName.replace('#'+slave+'#', '#'+master+'#');
    masterSelect=findElementsByName(masterSelectName)[0];
    masterSelectValue=masterSelect.options[masterSelect.selectedIndex].text;
    select.options.length=0;
    for(var m=1 ; m<array.length ; m++) {
        if (array[m][0]==masterSelectValue) {
            _addOption(select,'?', '?');
            for(var i=1 ; i<array[m].length ; i++) {
                _addOption(select, array[m][i], array[m][i]==selected);
            }
        }
    }
}

/* This is called from the onChange event from a 'master' dropdown in a 
 * master/slave relationship. It updates the slave's values appropriate
 * to the newly selected master value.
 */
function updateSlaveChoiceOptions(masterSelect, array, master, slave) {
    masterSelectValue=masterSelect.options[masterSelect.selectedIndex].text;
    slaveSelectName=new String(masterSelect.name);
    slaveSelectName=slaveSelectName.replace('#'+master+'#','#'+slave+'#');
    slaveSelect=findElementsByName(slaveSelectName)[0];
    slaveSelect.options.length=0;
    for(var m=1 ; m<array.length ; m++) {
        if (array[m][0]==masterSelectValue) {
            _addOption(slaveSelect,'?', '?');
            for(var i=1 ; i<array[m].length ; i++) {
                _addOption(slaveSelect, array[m][i], false);
            }
        }
    }
}

tinyMCE.init({
	// General options
	mode : "textareas",
	//elements : "elm2",
	theme : "advanced",
	skin : "o2k7",
	plugins : "safari,style,iespell",

	// Theme options
	theme_advanced_buttons1 : "bold,italic,underline,strikethrough",
	theme_advanced_buttons2 : "",
	theme_advanced_buttons3 : "",
	theme_advanced_toolbar_location : "external",
	theme_advanced_resizing_min_height : 50
});
