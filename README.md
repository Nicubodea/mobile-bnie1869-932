# README #

Nicolae Bodea's repository for mobile apps course
bnie1869 - 932 group

Application description:
The main purpose of the application will be to increase the usage of bike renting systems (like Cluj Bike, for example), while making it easier for the user to rent a bike and trying to simplify the birocracy behind getting a card for renting a bike.

The application use cases will be:
	1) Log-in/Register - one needs to authenticate in order to be able to use the app
	2) Renting a bike - one can declare intent of renting a bike before actually renting it
				- the user can declare the bike as rented in max one hour -> the bike-renting place is updated (has a bike less) and the bike becomes rented
				- the server will be called to reserve the bike
	3) Query the bike-rental places around me - depending on location, one can use the app to locate bike-renting places
						- this can also be used offline, the app will save coordinates for places ~10 km around you
	4) Verify if there are bikes in a bike-rental place
	5) Administrator mode - an administrator can delete bike places/add bike places/change bike places (e.g. how many bikes are in a bike-renting place, etc.)