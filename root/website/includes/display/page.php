<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<?php
include($includesPath."display/pagetitle.php"); 
?>

<body>
<!-- wrap starts here -->
<div id="wrap">

<?php
include($includesPath."display/pageheader.php"); 
?>
				
	<!-- content-wrap starts here -->
	<div id="content-wrap"><div id="content">		
		
<?php
if($sideMenuExclude==false){
  include($includesPath."display/pageside.php"); 
}
?>

<?php
include($includesPath."display/pagecontent".$currentPage.".php"); 
?>
	
		
	<!-- content-wrap ends here -->		
	</div></div>

<!-- footer starts here -->	
<?php
include($includesPath."display/pagefooter.php"); 
?>
<!-- footer ends here -->
	
<!-- wrap ends here -->
</div>

</body>
</html>
