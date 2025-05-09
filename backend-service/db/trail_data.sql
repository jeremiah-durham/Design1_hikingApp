USE project;

INSERT INTO parks (id, park_name)
VALUES
	(1, "Windy Saddle Park"),
	(2, "Mount Galbraith Park"),
	(3, "Golden Gate Canyon State Park");
INSERT INTO trails (park_id, trail_name, distance, elevation_delta, difficulty, est_time_min, lat, lon)
VALUES
	(1, "Lookout Mountain Trail via Windy Saddle Trailhead", 3, 521, "moderate", null, 39.73674,-105.24556),
	(1, "Chimney Gulch and Lookout Mountain Trail", 6.9, 1761, "moderate", 230.0, null,null),
	(1, "Lookout Mountain Trail", 4.4, 777, "moderate", 124.0, 39.73679,-105.24546),
	(1, "Beaver Brook Trail", 13.4, 3297, "hard", null, 39.73679,-105.24546),
	(1, "Mount Zion Trail", 0.5, 242, "moderate", 24.0, 39.7369,-105.24554),
	(1, "Buffalo Bill and Beaver Brook Trail", 13.6, 2286, "hard", null, 39.73366,-105.23925),
	(1, "Canal Zone via Grant Terry Trail", 2.7, 147, "easy", 55.0, 39.75048,-105.23126),
	(1, "Tiers of Zion Lower Trail", 0.7, 160, "moderate", 21.0, 39.74787,-105.24391),
	(2, "Mount Galbraith Loop Via Cedar Gulch Trail", 4.2, 928, "moderate", 131.0, 39.77365,-105.254),
	(2, "Nightbird Gulch Trail", 3.4, 872, "moderate", 113.0, 39.7615,-105.2377),
	(2, "Cedar Gulch, Mount Galbraith, and Nightbird Trail", 7.5, 1801, "moderate", 243.0, null,null),
	(2, "Cedar Gulch and Nightbird Gulch Trail", 5.9, 1453, "moderate", 193.0, 39.7735,-105.25406),
	(3, "Raccoon Trail", 2.6, 462, "moderate", 74.0, 39.87585,-105.44071),
	(3, "Panorama Point via Mule Deer Trail and Raccoon Trail", 5, 1020, "moderate", 150.0, 39.85806,-105.44751),
	(3, "Frazer Meadow via Horseshoe and Mule Deer Loop", 4, 954, "moderate", 128.0, null,null),
	(3, "Mountain Lion Trail", 6.8, 1578, "moderate", 217.0, 39.85004,-105.36029),
	(3, "Ralston Roost via Black Bear and Horseshoe Loop", 4.2, 1187, "hard", null, 39.83298,-105.40845),
	(3, "Black Bear and Horseshoe Trail Loop", 4.6, 1204, "moderate", 154.0, 39.83595,-105.40519),
	(3, "Snowshoe Hare Trail", 3, 659, "moderate", 93.0, 39.86443,-105.41647),
	(3, "Windy Peak via Mountain Lion Loop", 7.8, 1965, "hard", null, null,null),
	(3, "Frazer Meadow via Blue Grouse Trail", 4.5, 728, "moderate", 123.0, null,null),
	(3, "Black Bear/Horseshoe Loop", 6, 1433, "moderate", 193.0, null,null),
	(3, "Black Bear trail, Horseshoe Trail and Mule Deer Trail Loop", 5, 1246, "moderate", 164.0, null,null),
	(3, "Windy Peak via Mountain Lion Trail", 6.2, 1646, "moderate", null, 39.84612,-105.37849),
	(3, "Burro Trail Loop to Windy Peak", 6.5, 1774, "hard", null, 39.84607,-105.37849),
	(3, "Windy Peak via Mountain Lion and Burro Trail Loop", 6.2, 1696, "hard", 214.0, 39.84611,-105.37845),
	(3, "Upper Mule Deer Trail", 3.7, 501, "easy", 95.0, 39.85019,-105.44538),
	(3, "Coyote Trail to Mule Deer Trail North Loop", 6.8, 1240, "moderate", 195.0, null,null),
	(3, "Burro Loop Trail", null, null, "moderate", 165.0, 39.84614,-105.37845),
	(3, "Tallman Ranch via Bridge Creek Trailhead", 3.1, 531, "moderate", 87.0, null,null),
	(3, "Mule Deer trail", 9, 1404, "moderate", 243.0, null,null),
	(3, "Coyote Trail", 3.9, 967, "hard", null, 39.67614,-107.69945),
	(3, "Mounain Lion Trail to Burro Trail Loop", 6, 1689, "moderate", 210.0, null,null),
	(3, "Blue Grouse to Mule Deer Trail Loop", 10.4, 1601, "hard", 279.0, 39.8355,-105.42975),
	(3, "Beaver Trail", 3.4, 925, "moderate", 117.0, 39.83105,-105.42202),
	(3, "Buffalo Trail", null, 679, "moderate", 96.0, 39.86718,-105.40382),
	(3, "Blue Grouse Trail", 1.4, 249, "easy", 40.0, null,null),
	(3, "Mule Deer to Coyote Trail Loop", 5.7, 1043, "moderate", null, 39.85822,-105.44746),
	(3, "Frazer Meadow via Mule deer Trail", 4.7, 633, "moderate", 120.0, null,null),
	(3, "Upper Mule Deer and Lower Mule Deer Loop", 5, 672, "moderate", 128.0, 39.85024,-105.44526),
	(3, "Mountain Lion Trail and Burro Trail Loop", 5.4, 1243, "moderate", 171.0, 39.85046,-105.36027),
	(3, "Mountain Lion Trail to Quarry", 3.8, 797, "moderate", 115.0, null,null),
	(3, "Dude's Fishing Hole", 0.9, 154, "easy", 26.0, null,null),
	(3, "Black Bear, Mule Deer, Horseshoe Trail Loop", 9.3, 2129, "hard", 294.0, null,null),
	(3, "Burro Trail to City Lights Ridge", 2.8, 843, "moderate", 102.0, 39.8461,-105.37848),
	(3, "Blue Grouse and Mule Deer Loop", 10.6, 1765, "hard", 293.0, 39.83567,-105.42958),
	(3, "Snowshoe Hare and Mule Deer from Aspen Meadows Campground", 12.7, 2362, "hard", 367.0, null,null),
	(3, "Golden Gate Canyon Full Pull Trail", 27.7, 5810, "hard", 842.0, 39.83562,-105.42969),
	(3, "Visitor Center Nature Trail", 0.8, 111, "easy", 20.0, 39.83155,-105.41021),
	(3, "Mountain Lion, Windy Peak, and Burro Loop", 6.5, 1706, "moderate", 219.0, null,null),
	(3, "Kriley Pond", 0.7, 78, "easy", 17.0, 39.83552,-105.42981),
	(1, "Test Trail", 0.5, 30, "easy", 1.0, null,null);
INSERT INTO traits (trail_id, hiking, biking, mountain_views, river, forest, hist_sites, lake)
VALUES
	(1, true, true, true, false, true, true, false),
	(2, true, true, true, false, true, false, false),
	(3, true, true, true, false, true, false, false),
	(4, true, false, true, true, false, false, false),
	(5, true, false, true, false, true, false, false),
	(6, true, false, true, true, true, true, false),
	(7, true, false, true, true, true, false, false),
	(8, true, false, true, false, true, false, false),
	(9, true, false, true, false, true, false, false),
	(10, true, false, true, false, false, false, false),
	(11, true, false, true, false, false, false, false),
	(12, true, false, true, false, true, false, false),
	(13, true, false, true, true, true, false, false),
	(14, true, false, true, false, true, false, false),
	(15, true, false, true, false, true, true, false),
	(16, true, false, true, true, true, false, false),
	(17, true, false, true, false, true, false, false),
	(18, true, false, true, false, true, false, false),
	(19, true, true, true, false, true, true, true),
	(20, true, false, true, true, true, false, true),
	(21, true, false, true, false, true, true, false),
	(22, true, false, true, false, true, true, false),
	(23, true, false, true, false, true, false, false),
	(24, true, false, true, true, true, false, true),
	(25, true, false, true, false, true, false, false),
	(26, true, false, true, false, true, false, false),
	(27, true, false, true, false, true, false, false),
	(28, true, false, true, false, true, true, false),
	(29, true, false, true, false, true, false, false),
	(30, true, false, true, false, true, false, false),
	(31, true, false, true, false, true, true, false),
	(32, true, false, true, false, true, true, false),
	(33, true, false, true, false, true, false, false),
	(34, true, false, true, false, true, true, false),
	(35, true, false, true, false, true, false, false),
	(36, true, false, true, false, true, false, true),
	(37, true, false, true, false, true, false, false),
	(38, true, false, true, false, true, true, false),
	(39, true, false, true, false, true, true, false),
	(40, true, false, true, false, true, true, false),
	(41, true, false, true, false, true, false, false),
	(42, true, false, true, false, true, false, false),
	(43, true, false, true, false, false, true, true),
	(44, true, false, true, false, true, true, false),
	(45, true, false, true, false, false, false, false),
	(46, true, true, true, false, true, true, false),
	(47, true, true, true, false, true, true, false),
	(48, true, true, true, false, true, true, false),
	(49, true, true, true, false, true, false, false),
	(50, true, false, true, false, true, false, true),
	(51, true, false, true, false, false, false, true),
	(52, true, true, true, true, true, true, true);
