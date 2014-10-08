		<div id="sidebar" >

			<div class="sidebox">	
						
				<h1>What is Open Source?</h1>
				<p>What does it mean and how can it help your business?<br/><a href="opensource.php">Find out here »</a></p>	
				
			</div>
			
			<div class="sidebox">	
						
				<h1>Search Site</h1>	
				<form method="get" action="http://www.google.com/search" class="searchform">
					<p>
					<input type="hidden" name="ie" value="UTF-8" />
					<input type="hidden" name="oe" value="UTF-8" />
					<input type="hidden" name="domains" value="http://oquote.sourceforge.net/" />
					<input type="hidden" name="sitesearch" value="http://oquote.sourceforge.net/" />
					<input name="q" class="textbox" type="text" />
  					<input name="search" class="button" value="Search" type="submit" />
					</p>			
				</form>
						
				<h1>Search Project</h1>	
				<form method="get" action="http://sourceforge.net/search/" class="searchform">
					<p>
					<input type="hidden" name="type_of_search" value="pervasive" />
					<input type="hidden" name="group_id" value="215808" />
					<input name="words" class="textbox" type="text" />
  					<input name="search" class="button" value="Search" type="submit" />
					</p>			
				</form>
				
			</div>
					
		
			<div class="sidebox">
			
				<h1>News</h1>
				
				<?php
				include("http://sourceforge.net/export/projnews.php?group_id=215808&limit=4&flat=0&show_summaries=0"); 
				?>
						
			</div>			

		
			<div class="sidebox">
			
				<h1>Project</h1>
				
				<?php
				include("http://www.sourceforge.net/export/projhtml.php?group_id=215808&mode=full&no_table=1"); 
				?>
						
			</div>			



		</div>	

