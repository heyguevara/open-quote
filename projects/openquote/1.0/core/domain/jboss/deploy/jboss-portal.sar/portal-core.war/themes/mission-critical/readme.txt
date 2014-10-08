JBoss Portal theme information
November 15, 2005

Ingredients:
1) Theme name - "Mission Critical"
2) Theme notes - Description or concept of theme.
3) Custom branding - Removal of the JBoss branding graphic in the header.
4) Theme author/designer - Paul Tamaro, Novell
5) Additional credits

THEME NOTES:
Mission Critical was designed to present the versatility of the CSS-driven DIV-Renderer method of controlling the portal UI. The original idea and concept was "Business Bond." What would James Bond's portal look like anyway? I think it would be far "cooler" than this... Maybe I'll revisit this one after churning-out a few more themes, and see if I can improve it.

This theme does not include styles for navigation, or login.

CUSTOM BRANDING:
To remove, replace or hide the header graphic that contains the JBoss product branding text (the "JBoss Portal" text in the top-left-hand corner of the screen), open the "portal-styles.css" file and modify the #logoName selector.

#logoName {
/* Logo...*/
	background-image: url(images/portal-header.gif);
	background-repeat: no-repeat;
	width: 440px;
	height: 440px;
	z-index: 0;
	position: absolute;
	left: 0px;
	top: -1px;
}

You can easily just replace the background image with a custom graphic, or simply comment it out. Adjust the height and width attributes accordingly when replacing this image if necessary.

CREDITS:
* Concept, artwork, and CSS coding by Paul Tamaro. Released under Creative Commons License (by-sa).
* Copyright GNU LGPL (c) 2005 Novell, Inc.