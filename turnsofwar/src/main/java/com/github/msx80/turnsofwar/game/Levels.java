package com.github.msx80.turnsofwar.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.msx80.turnsofwar.animations.Animation;
import com.github.msx80.turnsofwar.animations.AnimationFactory;
import com.github.msx80.turnsofwar.animations.IAnimation;
import com.github.msx80.turnsofwar.ui.DialogType;

public class Levels {
    public static final Level[] ALL = new Level[] {   
   
	/*
	 * Note: in the porting i enabled units definition directly in the map.
	 * But i haven't moved all unit from here to the map because it's a pain in the ass and i'm lazy
	 * This is the reason why some are defined here and some on the map. 
	 * 
	 */
    
    	new Level(12, 17, new String[]{"## TUTORIAL: MOVING", "", "Help the Farmer reach the", "other side of the map", "", "The yellow circles show", 
        		"where you can move."}, () -> Game.unitInTargetArea())
        		.addTrap(new ColumnTrap(5, () -> 
        {
        	Animation unitAppear = AnimationFactory.makeUnitAppear(new Coord(0, 3), Units.RAT, false, () -> {
        		 Unit u = Game.addUnit(Units.RAT, 0, 3, 1);
                 if (u != null) 
                 {
                 	u.eff.put(Effects.SPENT, 2); // trucco per farlo apparire SPENT al turno successivo
                 }
                 Game.showDialog(List.of("Uh uh, better hurry up!"), null, DialogType.POPUP);
        	});
        	
        	 
           
            return new Event( unitAppear ); //new Event(Animation.sequential(unitAppear, dialog, null));
        }))
        		
        ,
        
         new Level(0, 17, new String[]{"## TUTORIAL: ATTACK", "", "Phew that was close!", "", "The Farmer encountered a", "kind Dwarf. He accepted to", "protect him during the travel.", "", "Get rid of all the enemies.", "", "The red targets show", "where you can attack."}, 
        		Game::noMoreEnemies)
         
         ,
        
        new Level(24, 17, new String[]{"## TUTORIAL: FREE ACTIONS", "", "Some units have free actions.", "Free actions don't spend the", "unit!", "They're shown on the info box.", "Here, the soldier has an", "extra, free Move action!", "", "Note: any other action will", "spend the unit, so use them", "first!"}, 
        		Game::noMoreEnemies),

        
        new Level(36, 17, new String[]{"## TUTORIAL: RANGED ATTACKS", "", "Some units have ranged", "attack! Their range can be", "seen in the info box or", "drawn on the map.", "", "Help the elves defend from", "the Skeletons!"}, 
        		Game::noMoreEnemies),

        new Level(48, 17, new String[]{"## TUTORIAL: MAGIC", "", "Some units can cast spells!", "They can give effects or", "alter the match in many", "ways.", "", "Use your Mage ability to", "withstand the assault", "at the village!", "", "Positive spells usually", "have green circle target."}, 
        		Game::noMoreEnemies),

        new Level(60, 17, new String[]{"## TUTORIAL: COOLDOWN", "", "Some abilities require", "some rest before being", "used again. You can", "check it on the info", "box.", "", "A Druid alone doesn't", "look like much, but..."}, 
        		Game::noMoreEnemies),


        new Level(72, 17, new String[]{"## SKELETON'S ATTACK!", "", "It's a pleasant morning", "when, unexpectedly, some", "Skeletons attack the", "village! We have some", "archers but they're not", "many.", "", "Try to keep skeletons busy", "while the elves shoot them!"}, 
        		Game::noMoreEnemies).addTrap(new ColumnTrap(2, () -> 
        	{ Game.showDialog(List.of("\"Why are they attacking?", "", "We never had much skeleton", "problems around here. I", "wonder why they're showing", "up now..\""), null, DialogType.POPUP);
        	return null;
        	}
        	))
        ,


        new Level(84, 17, new String[]{"## EVEN MORE SKELETONS!", "", "They have some kobolds too!", "Keep the mage out of their", "range!"}, Game::noMoreEnemies)
        .addGood(Units.MAGE, 0, 3)
        .addGood(Units.SOLDIER, 1, 3)
        .addGood(Units.SOLDIER, 1, 4)
        .addGood(Units.ELF, 1, 6)
        .addGood(Units.ELF, 1, 2)
        .addGood(Units.ELF, 2, 7)
        .addEvil(Units.SKELETON, 10, 4)
        .addEvil(Units.KOBOLD, 10, 3)
        .addEvil(Units.SKELETON, 9, 4)
        .addEvil(Units.SKELETON, 10, 5)
        .addEvil(Units.SKELETON, 10, 2)
        .addEvil(Units.SKELETON, 9, 2)
        .addEvil(Units.SKELETON, 10, 6)
        .addEvil(Units.SKELETON, 9, 3)
        .addEvil(Units.KOBOLD, 9, 5),
        


        new Level(96, 17, new String[]{"## THE RUN", "", "The skeleton attacks are", "intensifying. We need to", "investigate and find cause!", "", "The road out of the village", "is dangerous. Luckly,", "one of the Slug people", "accepted to aid us", "with the run!"}, () -> Game.unitInTargetArea())
        .addGood(Units.ELF, 1, 0)
        .addGood(Units.CAPTAIN, 0, 0)
        .addGood(Units.MAGE, 1, 3)
        .addGood(Units.SOLDIER, 8, 7)
        .addGood(Units.SLUGDO, 0, 1)
        .addGood(Units.SOLDIER, 7, 7)
        .addEvil(Units.SPIDER, 10, 3)
        .addEvil(Units.KOBOLD, 7, 0)
        .addEvil(Units.KOBOLD, 8, 0)
        .addEvil(Units.SNAKE, 6, 3)
        .addEvil(Units.SNAKE, 7, 3)
        .addEvil(Units.SNAKE, 6, 4)
        .addEvil(Units.SNAKE, 5, 1)
        .addEvil(Units.SKELETON, 1, 7)
        .addEvil(Units.RAT, 2, 7)
        .addEvil(Units.SKELETON, 0, 7)
        .addEvil(Units.SKELETON, 1, 6)
        .addEvil(Units.SNAKE, 0, 6)
        .addEvil(Units.RAT, 1, 5)
        .addEvil(Units.RAT, 0, 5)
        ,

        new Level(108, 17, new String[]{"## THE LICHKING", "", "Here's the source of all", "those skeletons! A Lichking", "has arrived to the village!", "", "Be quick to take it down as", "he can summon more skeletons!"}, Game::noMoreEnemies)
        .addGood(Units.ELF, 1, 3)
        .addGood(Units.CAPTAIN, 0, 4)
        .addGood(Units.SOLDIER, 1, 4)
        .addGood(Units.SLUGDO, 0, 3)
        .addGood(Units.SOLDIER, 0, 5)
        .addEvil(Units.LICHKING, 9, 4)
        .addEvil(Units.SKELETON, 9, 3)
        .addEvil(Units.SKELETON, 9, 5)
        ,

        new Level(120, 17, new String[]{"## BRUTE ENCOUNTER", "", "On the way back to the", "village, the fellowship has", "to cross a mountain pass.", "", "Brutes inhabit the area,", "Be careful as they're pretty", "tough!", "", "A solitary monk living there", "can provide some help!"}, Game::noMoreEnemies)
        .addGood(Units.ELF, 1, 3)
        .addGood(Units.CAPTAIN, 0, 4)
        .addGood(Units.MONK, 1, 1)
        .addGood(Units.SOLDIER, 1, 4)
        .addGood(Units.SOLDIER, 0, 5)
        .addEvil(Units.BRUTE, 8, 4)
        .addEvil(Units.BRUTE, 9, 3)
        .addEvil(Units.BRUTE, 10, 5)
        .addEvil(Units.BRUTE, 10, 4)
        ,
        

        new Level(132, 17, new String[]{"## UH OH!", "", "A couple of Kobolds ambushed", "your team! And some other", "thugs are joining the party!", "", "This is a tough one, plan", "carefully and exploit the", "wizard!"}, Game::noMoreEnemies)
        .addGood(Units.MAGE, 1, 3)
        .addGood(Units.SLUGDO, 0, 4)
        .addGood(Units.MONK, 0, 3)
        .addGood(Units.SOLDIER, 1, 4)
        .addGood(Units.SOLDIER, 0, 5)
        .addEvil(Units.KOBOLD, 8, 4)
        .addEvil(Units.KOBOLD, 8, 3)
        .addEvil(Units.GOBLIN, 10, 5)
        .addEvil(Units.GOBLIN, 10, 4)
        .addEvil(Units.BRUTE, 10, 1)
        .addEvil(Units.BRUTE, 7, 7)
        ,

        new Level(144, 17, new String[]{"## DWARF RESQUE", "", "A dwarf has been attacked", "by a bunch of Kobolds!", "", "Perhaps if we resque him", "he may help us later.", "", "Try to save him in the first", "turns as he's indispensable", "to defeat all those enemies!"}, Game::noMoreEnemies)
        .addGood(Units.MAGE, 1, 2)
        .addGood(Units.SLUGDO, 2, 2)
        .addGood(Units.SOLDIER, 1, 3)
        .addGood(Units.SOLDIER, 0, 4)
        .addGood(Units.DWARF, 7, 4)
        .addEvil(Units.KOBOLD, 8, 4)
        .addEvil(Units.KOBOLD, 7, 3)
        .addEvil(Units.KOBOLD, 7, 5)
        .addEvil(Units.KOBOLD, 10, 5)
        .addEvil(Units.GOBLIN, 10, 4)
        
        ,
        

        // #14 DWARFS VS BRUTES
        new Level(156, 17, new String[]{"## DWARFS VS BRUTES", "", "Take advantage of the", "bottleneck to show them", "brutes who's the boss", "of the mountains!", "", "Also, BOMBS!"}, Game::noMoreEnemies)
        .addGood(Units.ELF, 2, 2)
        .addGood(Units.CAPTAIN, 1, 2)
        .addGood(Units.DWARF, 2, 3)
        .addGood(Units.BOMBER, 1, 4)
        .addEvil(Units.KOBOLD, 6, 4)
        .addEvil(Units.BRUTE, 8, 4)
        .addEvil(Units.BRUTE, 8, 3)
        .addEvil(Units.BRUTE, 8, 5)
        .addEvil(Units.BRUTE, 6, 5)
        .addEvil(Units.BRUTE, 10, 3)
        .addEvil(Units.GOBLIN, 10, 4)
        ,

        
            new Level(168, 17, new String[]{"## UNDER ATTACK", "", "The tables have turned:", "now WE are under attack", "from ranged enemies!", "", "No subtleties here, just", "rush onto them!"}, Game::noMoreEnemies)
            .addGood(Units.DWARF, 1, 2)
            .addGood(Units.DWARF, 1, 3)
            .addGood(Units.SLUGDO, 2, 2)
            .addGood(Units.DWARF, 1, 4)
            .addEvil(Units.KOBOLD, 9, 4)
            .addEvil(Units.KOBOLD, 8, 4)
            .addEvil(Units.KOBOLD, 9, 3),

            new Level(132, 26, new String[]{
            		"## ROCK AND ROLL", "", 
            		"There's a gorge full of enemies!",
            		"We are outnumbered! Our only hope",
            		"is a Stonecaster, one of those",
            		"strange magical living rocks", 
            		"that are said to be able to",
            		"control stone itself!","",
            		"Let's try and block their",
            		"advancement!"}, Game::noMoreEnemies)
            .addGood(Units.STONECASTER, 0, 2)
            .addGood(Units.ELF, 1, 3)
            .addGood(Units.ELF, 2, 2)
            .addGood(Units.CAPTAIN, 1, 4)
            .addGood(Units.SLUGDO, 2, 3)
            .addEvil(Units.GOBLIN, 9, 4)
            .addEvil(Units.BRUTE, 8, 4)
            .addEvil(Units.BRUTE, 9, 3)
            .addEvil(Units.RAT, 9, 2)
            .addEvil(Units.RAT, 8, 2)
            .addEvil(Units.BRUTE, 7, 7)
            
            ,
        
 new Level(144, 26, new String[]{
            		"## BLOODTHIRST", "", 
            		"We're amost back at the village.",
            		"But in this land there's an ancient",
            		"graveyard..",
            		"Be careful, there's no telling what",
            		"kind of monsters you can find."
            		}, Game::noMoreEnemies)
 ,
    
            

        new Level(180, 17, new String[]{"## BACK AT THE VILLAGE", "", "Oh no! The village has been", "Overrun by the forces of evil!"}, Game::noMoreEnemies)
        .addGood(Units.SOLDIER, 2, 2)
        .addGood(Units.SOLDIER, 2, 3)
        .addGood(Units.SLUGDO, 3, 2)
        .addGood(Units.ELF, 0, 3)
        .addGood(Units.CAPTAIN, 0, 4)
        .addGood(Units.DWARF, 2, 4)
        .addGood(Units.BOMBER, 1, 4)
        .addGood(Units.MAGE, 1, 3)
        .addGood(Units.DRUID, 1, 2)
        .addEvil(Units.SKELETON, 9, 5)
        .addEvil(Units.SKELETON, 8, 5)
        .addEvil(Units.KOBOLD, 9, 3)
        .addEvil(Units.KOBOLD, 10, 2)
        .addEvil(Units.LICHKING, 10, 3)
        .addEvil(Units.LICHKING, 10, 4)
        .addEvil(Units.GOBLIN, 10, 5)
        .addEvil(Units.KOBOLD, 10, 6)
        ,
        

        // #17 SEARCHING FOR HELP
        new Level(192, 17, new String[]{"## SEARCHING FOR HELP", "", "The village destroyed, you", "have to rally some forces..", "You try and head for a", "nearby monastry, where a", "different kind of monks", "dwells. The fighting kind!", "", "Beat them to earn their", "respect and loyalty!"}, Game::noMoreEnemies)
        .addGood(Units.SOLDIER, 2, 2)
        .addGood(Units.DWARF, 2, 4)
        .addGood(Units.MONK, 1, 4)
        .addGood(Units.MAGE, 1, 3)
        .addEvil(Units.SHAOLIN, 7, 4)
        .addEvil(Units.SHAOLIN, 8, 3)
        .addEvil(Units.SHAOLIN, 9, 5)
        .addEvil(Units.SHAOLIN, 8, 5)
,
        new Level(204, 17, new String[]{"## SURROUNDED BY BRUTES", "", "Wow, those kids were fast", "as lighting! Good that we", "won their respect!", "", "Looks like some brutes are", "trying to surround us.", "Teach em a lesson."}, Game::noMoreEnemies)
        .addGood(Units.SOLDIER, 4, 2)
        .addGood(Units.SHAOLIN, 5, 4)
        .addGood(Units.SHAOLIN, 6, 4)
        .addGood(Units.MAGE, 5, 3)
        .addGood(Units.MONK, 5, 5)
        .addEvil(Units.BRUTE, 4, 0)
        .addEvil(Units.BRUTE, 9, 3)
        .addEvil(Units.BRUTE, 8, 4)
        .addEvil(Units.BRUTE, 9, 5)
        .addEvil(Units.BRUTE, 8, 6)
        .addEvil(Units.BRUTE, 0, 5)
        .addEvil(Units.BRUTE, 0, 6),

        
        new Level(216, 17, new String[]{"## CYCLOPS", "", "Uh oh, looks who's there..", "Those guys are pretty", "big and strong.", "", "Luckly the Shaolin are", "fast enought to sting and", "back off."}, Game::noMoreEnemies)
        .addGood(Units.SOLDIER, 4, 2)
        .addGood(Units.SHAOLIN, 5, 4)
        .addGood(Units.SHAOLIN, 6, 4)
        .addGood(Units.MAGE, 5, 3)
        .addEvil(Units.CYCLOP, 9, 3)
        .addEvil(Units.CYCLOP, 8, 4)
        .addEvil(Units.SNAKE, 8, 6)
        .addEvil(Units.SNAKE, 0, 5)
        .addEvil(Units.SNAKE, 0, 6),

 
        new Level(228, 17, new String[]{"## CALL IN THE PALADIN", "", "We arrived at the remote", "retire of a powerfull paladin.", "", "If we can get past all those", "monsters, and if we can find", "him maybe we can hire him!"}, () -> Game.unitInTargetArea())
        .addGood(Units.SOLDIER, 1, 3)
        .addGood(Units.SHAOLIN, 1, 4)
        .addGood(Units.SLUGDO, 0, 4)
        .addGood(Units.BOMBER, 0, 3)
        .addGood(Units.ELF, 0, 2)
        .addGood(Units.MAGE, 0, 5)
        .addGood(Units.MONK, 1, 6)
        .addEvil(Units.CYCLOP, 9, 3)
        .addEvil(Units.CYCLOP, 8, 4)
        .addEvil(Units.SNAKE, 9, 6)
        .addEvil(Units.CYCLOP, 10, 6)
        .addEvil(Units.RAT, 7, 1)
        .addEvil(Units.RAT, 8, 1)
        .addEvil(Units.RAT, 8, 2)
        .addEvil(Units.RAT, 9, 5)
        .addEvil(Units.RAT, 10, 3)
        .addEvil(Units.SPIDER, 8, 3)
        .addEvil(Units.KOBOLD, 9, 4)
        ,

        new Level(84, 26, new String[]{"## ROAD TO THE KING", "", "With the paladin on our side", "we'll be able to get the King", "attention and involvement!", "", "It's along journey to the", "castle tho, better start", "walking..."}, Game::noMoreEnemies)
        .addGood(Units.ELF, 0, 1)
        .addGood(Units.PALADIN, 1, 0)
        .addGood(Units.BOMBER, 0, 0)
        .addEvil(Units.SLIME, 10, 7)
        .addTrap(new ColumnTrap(2, () -> { 
        	Game.showDialog(List.of("", "\"Ew.. What's that thing?", ""), null, DialogType.POPUP);
        	return null;
        })),
        

        
        new Level(96, 26, new String[]{"## EVEN MORE SLIME!", "", "Ew, those blobs were", "disgusting! And it looks like", "there's even more of them", "here! They spread like crazy!", "", "We must eradicate them asap", "or they'll grow out of", "control!", "", "We have few units, but we", "can use these litte doggies!"}, Game::noMoreEnemies)
        .addGood(Units.ELF, 0, 1)
        .addGood(Units.PALADIN, 1, 1)
        .addGood(Units.BOMBER, 1, 2)
        .addGood(Units.DWARF, 0, 2)
        .addGood(Units.DOG, 0, 3)
        .addGood(Units.DOG, 1, 3)
        .addEvil(Units.SLIME, 10, 2)
        .addEvil(Units.SLIME, 10, 4)
        .addEvil(Units.SLIME, 10, 5)
        .addEvil(Units.SLIME, 9, 3)
        ,

        new Level(108, 26, new String[]{"## TENTACLES", "", "I'm glad we left those blobs", "behind.", "Now there's a dangerous river", "to cross, it's populated with", "wild river octopuses ready", "to attack!", "", "Be careful!"}, 
        		Game::noMoreEnemies)
        ,


        
        new Level(156, 26, new String[]{
            		"## CENTAUR FOREST", "", 
            		"We need to cross a forest.", "We carefully step inside and", "are soon met with the sound", "of galloping horseshoe.",
            		"", "But those are no horses!"
            		}, Game::noMoreEnemies)
            
       	, 
        new Level(168, 26, new String[]{
            		"## MORE VAMPIRES!", "", 
            		"Your party stumbles upon a",
            		"gathering of Vampires!",
            		"",
            		"Luckly, some rogue",
            		"Centaurs saw us fighting",
            		"against evil and decided we",
            		"are worth of their help!",
            		
            		"Be careful: Vampires move",
            		"very fast!"
            		}, Game::noMoreEnemies),
       

        new Level(120, 26, new String[]{"## ALMOST AT THE CASTLE", "", "We are in the vicinity of the", "King Castle! But first we have", "to pass the desolate lands of", "Kfnir, where a powerful god", "sleeps. Woe betide he who", "awakes him from his slumber!"}, Game::noMoreEnemies)
        .addGood(Units.PALADIN, 0, 1)
        .addGood(Units.SOLDIER, 0, 3)
        .addGood(Units.SLUGDO, 1, 2)
        .addGood(Units.ELF, 1, 1)
        .addGood(Units.ELF, 0, 4)
        .addEvil(Units.PDOR, 8, 3)
        .addTrap(new ColumnTrap(1, () -> {
            Game.showDialog(List.of("Pdor, son of Kmer:", "", "\"Who dares wake me up from", "my slumber??\""), () -> {
                Game.showDialog(List.of("Paladin:", "", "\"Wops!", "I think we are", "in trouble guys.\""), null, DialogType.POPUP);
            }, DialogType.POPUP);
            return null;
        })),
        

        new Level(0, 26, new String[]{
        		"#25 THE KING CASTLE", "", 
        		"We finally reached the castle!",
        		"With the paladin on our side", 
        		"we can ask for the king help!", "", 
        		"But wait! It looks like the", "castle is besieged by", "an army of monsters!", "", "Let's help our people!"}, Game::noMoreEnemies)
        .addGood(Units.SOLDIER, 1, 3)
        .addGood(Units.PALADIN, 1, 4)
        .addGood(Units.SOLDIER, 1, 5)
        .addGood(Units.DWARF, 0, 4)
        .addGood(Units.ELF, 1, 6)
        .addEvil(Units.GOBLIN, 9, 3)
        .addEvil(Units.GOBLIN, 8, 4)
        .addEvil(Units.KOBOLD, 8, 2)
        .addEvil(Units.KOBOLD, 10, 3)
        .addEvil(Units.RAT, 7, 1)
        .addEvil(Units.RAT, 8, 1)
        .addEvil(Units.SNAKE, 5, 7)
        .addEvil(Units.SNAKE, 4, 7),

        new Level(12, 26, new String[]{"## COUNTERATTACK!", "", "The king agreed to meet us", "and decided it's time to", "fight the monsters back", "and free the reign once", "again!", "", "We must find where they", "come from!"}, Game::noMoreEnemies)
        .addGood(Units.KING, 1, 3)
        .addGood(Units.PALADIN, 1, 4)
        .addGood(Units.SOLDIER, 1, 5)
        .addGood(Units.SOLDIER, 0, 5)
        .addGood(Units.SOLDIER, 0, 6)
        .addGood(Units.MONK, 0, 3)
        .addEvil(Units.CYCLOP, 9, 3)
        .addEvil(Units.BRUTE, 8, 4)
        .addEvil(Units.CYCLOP, 8, 2)
        .addEvil(Units.BRUTE, 10, 3)
        .addEvil(Units.BRUTE, 9, 4)
        .addTrap(new ColumnTrap(2, () -> { 
        	Game.showDialog(List.of("", "\"Wow, having the king", "on the battlefield", "gives a huge morale", "boost to our troops!", "", "We are unstoppable!\"", ""), null, DialogType.POPUP);
        	return null;
        })),

        new Level(24, 26, new String[]{"## THE WITCH", "", "We need some help to locate", "The source of the evilness", "that's ruining our land.", "", "A famous witch lives nearby", "in a dilapidated house.", "Perhaps her magic can", "help us!"}, () -> Game.unitInTargetArea())
        .addGood(Units.KING, 1, 3)
        .addGood(Units.PALADIN, 1, 2)
        .addGood(Units.SOLDIER, 0, 2)
        .addGood(Units.MONK, 0, 3)
        .addEvil(Units.KOBOLD, 6, 1)
        .addEvil(Units.KOBOLD, 7, 1)
        .addEvil(Units.KOBOLD, 2, 7)
        .addEvil(Units.KOBOLD, 3, 7)
        .addEvil(Units.KOBOLD, 5, 7)
        .addEvil(Units.BRUTE, 8, 4)
        .addEvil(Units.BRUTE, 9, 5)
        .addEvil(Units.RAT, 8, 3)
        .addEvil(Units.RAT, 9, 3)
        .addEvil(Units.SNAKE, 6, 3)
        .addEvil(Units.SNAKE, 6, 4),

        new Level(36, 26, new String[]{"## WITCHY WITCH", "", "The witch joins the battle.", "Sure she'll be useful, but", "you're not sure you like her.", "", "She need to reach a magical", "well where she can perform a", "divination and locate the", "monsters source.", "", "We're kind of surrounded here,", "Power up those elves and", "resist the assault!"}, 
        		Game::noMoreEnemies)       
        .addGood(Units.KING, 0, 0)
        .addGood(Units.ELF, 1, 0)
        .addGood(Units.SOLDIER, 1, 2)
        .addGood(Units.ELF, 0, 2)
        .addGood(Units.CAPTAIN, 1, 1)
        .addGood(Units.WITCH, 0, 1)
        .addEvil(Units.BRUTE, 8, 4)
        .addEvil(Units.BRUTE, 9, 5)
        .addEvil(Units.BRUTE, 10, 5)
        .addEvil(Units.BRUTE, 6, 2)
        .addEvil(Units.RAT, 8, 3)
        .addEvil(Units.RAT, 7, 7)
        .addEvil(Units.CYCLOP, 9, 3)
        .addEvil(Units.GOBLIN, 7, 3)
        .addEvil(Units.GOBLIN, 5, 6)
        .addEvil(Units.SNAKE, 7, 4),

        new Level(48, 26, new String[]{"## THE MAGICAL WELL", "", "We reached the well, move", "the witch nearby to perform", "the ritual!", "", "Looks like the forces of", "Evil got wind of our plans", "becouse monsters are", "literally pouring in!"}, 
        		() -> false // win is determined by tiletrap
        		)
        .addGood(Units.KING, 0, 0)
        .addGood(Units.ELF, 0, 3)
        .addGood(Units.SOLDIER, 1, 2)
        .addGood(Units.ELF, 0, 2)
        .addGood(Units.CAPTAIN, 1, 1)
        .addGood(Units.WITCH, 0, 1)
        .addEvil(Units.BRUTE, 8, 7)
        .addTrap(new ColumnTrap(4, () -> {
            Game.showDialog(List.of(
            		"Careful.. ","Only the witch has the ability", 
            		"to handle the power of the",
            		"magical well!", "", "Any other unit will just unleash", "the magic and destroy everything!"), null, DialogType.POPUP);
            return null;
        })).setTurner( () -> {
        	List<Unit> toAdd = new ArrayList<Unit>();
        	
            Unit u1 = Game.addNear(Units.SKELETON, 10, 5, 1);
            if (u1 != null) { u1.eff.put(Effects.SPENT, 1); toAdd.add(u1); }
            Unit u2 = Game.addNear(Units.SKELETON, 4, 7, 1);
            if (u2 != null) { u2.eff.put(Effects.SPENT, 1); toAdd.add(u2); }
            Unit u3 = Game.addNear(Units.SKELETON, 5, 0, 1);
            if (u3 != null) { u3.eff.put(Effects.SPENT, 1); toAdd.add(u3); }
            
            Optional<IAnimation> a = toAdd.stream()
            		.map(AnimationFactory::makeShakeAnim)
            		.reduce(Animation::parallel);
            
            if(a.isPresent())
            {
            	a.get().setOnBegin(() -> { UtilsToCleanup.sfx(11);});
            	Game.setAnimation(a.get());
            }
        }).addTrap(new TileTrap(7, 4, () -> {
            Unit u = Game.unitAt(7, 4);
            if (u != null && u.type == Units.WITCH) 
            {
            	
            	IAnimation killAllAnim = Game.units.stream()
            			.filter(Unit::isEvil)
            			.map(AnimationFactory::makeShakeAnim)
            			.reduce(Animation::parallel)
            			.orElse(null);
            	
            	Runnable onEnd;
            	
            	if(killAllAnim != null)
            	{
            		killAllAnim.setOnBegin(() -> { UtilsToCleanup.sfx(11);});
            		killAllAnim.setOnEnd(() -> {
            			// actually remove the units
            			Game.units.stream().filter(Unit::isEvil).collect(Collectors.toList()).forEach(Game::removeUnit);
            			// perform win
            			Game.doWin();
            		});
            		onEnd = () -> { Game.setAnimation(killAllAnim); };
            	}
            	else
            	{
            		onEnd = Game::doWin;
            	}
            	            	
                Game.showDialog(List.of("ABRA CADABRA!", "The witch performs a dark", "divination ritual!", "She observe a portal", "not too far away,", "from where the monsters", "are coming...", "", "We must close it!", "", "The energy blast from", "the ritual kills all", "enemies!"), onEnd, DialogType.POPUP);
            } else if (u != null) {
                Game.showDialog(List.of("ARGH!!", "\"Only a witch can withstand", "the power of the magic well!", "the unit dies in a huge", "explosion taking everybody", "with him!"), Game::doDefeat, DialogType.POPUP);
            }
            return null;
        })),

        new Level(60, 26, new String[]{"## THE PORTAL", "", "This is it, the final battle!", "", "The portal is here, and it has", "just spit out the strongest", "of all enemies, the Archdemon!", "", "We must kill all monsters and", "destroy the portal! While it is", "open, more skeletons will", "enter!", "", "Good luck, for our freedom!"}, Game::noMoreEnemies)
        .addGood(Units.KING, 0, 0)
        .addGood(Units.ELF, 0, 3)
        .addGood(Units.SOLDIER, 1, 2)
        .addGood(Units.ELF, 0, 2)
        .addGood(Units.CAPTAIN, 1, 1)
        .addGood(Units.WITCH, 0, 1)
        .addGood(Units.PALADIN, 0, 4)
        .addGood(Units.BOMBER, 0, 5)
        .addGood(Units.SLUGDO, 2, 0)
        .addGood(Units.DWARF, 1, 0)
        .addGood(Units.MAGE, 1, 4)
        .addGood(Units.DRUID, 1, 3)
        .addEvil(Units.ARCHDEMON, 10, 7)
        .addEvil(Units.LICHKING, 10, 6)
        .addEvil(Units.CYCLOP, 9, 7)
        .addEvil(Units.KOBOLD, 8, 6)
        .addEvil(Units.KOBOLD, 9, 5)
        .addEvil(Units.BRUTE, 6, 4)
        .addEvil(Units.BRUTE, 5, 5)
        .addEvil(Units.GOBLIN, 9, 3)
        .addEvil(Units.PORTAL, 9, 6)
        .setTurner(  () -> {
            if (Game.count(Units.PORTAL) > 0 && Game.count(Units.SKELETON) < 10) {

            	List<Unit> toAdd = new ArrayList<Unit>();
            	
                Unit u1 = Game.addNear(Units.SKELETON, 10, 5, 1);
                if (u1 != null) { u1.eff.put(Effects.SPENT, 1); toAdd.add(u1); }
                Unit u2 = Game.addNear(Units.SKELETON, 4, 7, 1);
                if (u2 != null) { u2.eff.put(Effects.SPENT, 1); toAdd.add(u2); }
                Unit u3 = Game.addNear(Units.SKELETON, 5, 0, 1);
                if (u3 != null) { u3.eff.put(Effects.SPENT, 1); toAdd.add(u3); }
                
                Optional<IAnimation> a = toAdd.stream()
                		.map(AnimationFactory::makeShakeAnim)
                		.reduce(Animation::parallel);
                
                if(a.isPresent())
                {
                	a.get().setOnBegin(() -> { UtilsToCleanup.sfx(11);});
                	Game.setAnimation(a.get());
                }
            }
        }),
        
        new Level(72, 26, new String[]{"## VICTORY!", "", "The portal has been closed!", "We WON the war against the", "evil forces!", "", "Once the dust settled, the", "celebrations concluded, ", "peace finally return to the", "lands."}, () -> false)
        .addGood(Units.KING, 0, 0)
        .addGood(Units.FARMER, 5, 6)
        .addGood(Units.FARMER, 6, 4)
        .addGood(Units.FARMER, 7, 2)
        .addGood(Units.SOLDIER, 0, 2)
        .addGood(Units.SOLDIER, 1, 2)
        .setTurner( () -> {
            if (Game.turnCount == 3) {
            
                Game.showDialog(List.of(
                		"The Game is Over", 
                		"", 
                		"Conglaturation!", 
                		"You defeated the evil portal",
                		"And now peace reigns!",
                		"", 
                		"You can move around the", 
                		"farmers but they're just", 
                		"there to signify peace. There", 
                		"are no more monsters to kill.", "", 
                		"If you liked this game, please", 
                		"leave a star on Github!", 
                		"Thanks :)"), 
                		() -> {
                    Game.showDialog(List.of(
                    		"A game by MSX", "", 
                    		"Reach me on the Fediverse at",
                    		"", 
                    		"- msx@livellosegreto.it", 
                    		"", 
                    		"Graphics by: ",
                    		"- Stavros (eliacoan @ twitter)",
                    		"", 
                    		"Music and sfx by:",
                    		"- Fubuki"), null, DialogType.POPUP);
                }, DialogType.POPUP);
            }
        })
        .addTrap(new TileTrap(10, 5, () -> {
        	
        	
            Game.showDialog(List.of("Uhm","", "Hey, the road continues", "down there!", "", "Uhuh let's see where it takes.."), Game::doWin, DialogType.POPUP);
            
            return null;
        })),
        
        
        new Level(180, 26, new String[]{"## LOL Secret Level!", "", "Good, good.", "You found the secret level", "you smart person!", "", "Have fun!"}, () -> false)
        .addTrap(new TileTrap(10,6, () -> {
        	
        	
            Game.showDialog(List.of("Hey!","", "The road continues again..", "", "Another secret level??"), Game::doWin, DialogType.POPUP);
            
            return null;
        })),
        		
        new Level(192, 26, new String[]{"## ROGER AGAINST ALL!", "", "Another secret level :D", "", "Roger is a chonky cat", "his power is beyond human", "comprehension.", "", "This is the last secret level, ok? :D", "Have fun!"}, () -> false)
    };
        
    static {
    	int i = 0;
        for (Level level : ALL) {
        	i++;
			level.intro[0] = level.intro[0].replace("##", "#"+i);
		}
        
    }
}