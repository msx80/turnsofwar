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
    public static final List<Level> ALL = new ArrayList<>();

    
    /*
     * Note: in the porting i enabled units definition directly in the map.
     * But i haven't moved all unit from here to the map because it's a pain in the ass.
     * This is the reason some are defined here and some on the map. 
     * 
     */
    
    static {
        // #1 TUTORIAL: MOVING
        Level l1 = new Level(12, 17, new String[]{"## TUTORIAL: MOVING", "", "Help the Farmer reach the", "other side of the map", "", "The yellow circles show", 
        		"where you can move."}, () -> Game.unitInTargetArea());
        l1.traps.add(new ColumnTrap(5, () -> 
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
        }));
        ALL.add(l1);
        
        ALL.add( new Level(0, 17, new String[]{"## TUTORIAL: ATTACK", "", "Phew that was close!", "", "The Farmer encountered a", "kind Dwarf. He accepted to", "protect him during the travel.", "", "Get rid of all the enemies.", "", "The red targets show", "where you can attack."}, 
        		Game::noMoreEnemies));
        
        ALL.add(new Level(24, 17, new String[]{"## TUTORIAL: FREE ACTIONS", "", "Some units have free actions.", "Free actions don't spend the", "unit!", "They're shown on the info box.", "Here, the soldier has an", "extra, free Move action!", "", "Note: any other action will", "spend the unit, so use them", "first!"}, 
        		Game::noMoreEnemies));

        
        ALL.add(new Level(36, 17, new String[]{"## TUTORIAL: RANGED ATTACKS", "", "Some units have ranged", "attack! Their range can be", "seen in the info box or", "drawn on the map.", "", "Help the elves defend from", "the Skeletons!"}, 
        		Game::noMoreEnemies));

        ALL.add(new Level(48, 17, new String[]{"## TUTORIAL: MAGIC", "", "Some units can cast spells!", "They can give effects or", "alter the match in many", "ways.", "", "Use your Mage ability to", "withstand the assault", "at the village!", "", "Positive spells usually", "have green circle target."}, 
        		Game::noMoreEnemies));

        ALL.add(new Level(60, 17, new String[]{"## TUTORIAL: COOLDOWN", "", "Some abilities require", "some rest before being", "used again. You can", "check it on the info", "box.", "", "A Druid alone doesn't", "look like much, but..."}, 
        		Game::noMoreEnemies));

        // #7 SKELETON'S ATTACK!
        Level l7 = new Level(72, 17, new String[]{"## SKELETON'S ATTACK!", "", "It's a pleasant morning", "when, unexpectedly, some", "Skeletons attack the", "village! We have some", "archers but they're not", "many.", "", "Try to keep skeletons busy", "while the elves shoot them!"}, Game::noMoreEnemies);
        l7.traps.add(new ColumnTrap(2, () -> 
        	{ Game.showDialog(List.of("\"Why are they attacking?", "", "We never had much skeleton", "problems around here. I", "wonder why they're showing", "up now..\""), null, DialogType.POPUP);
        	return null;
        	}
        	));
        ALL.add(l7);

        // #8 EVEN MORE SKELETONS!
        Level l8 = new Level(84, 17, new String[]{"## EVEN MORE SKELETONS!", "", "They have some kobolds too!", "Keep the mage out of their", "range!"}, Game::noMoreEnemies);
        l8.good.add(new Level.UnitSpawn(Units.MAGE, 0, 3));
        l8.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 3));
        l8.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 4));
        l8.good.add(new Level.UnitSpawn(Units.ELF, 1, 6));
        l8.good.add(new Level.UnitSpawn(Units.ELF, 1, 2));
        l8.good.add(new Level.UnitSpawn(Units.ELF, 2, 7));
        l8.evil.add(new Level.UnitSpawn(Units.SKELETON, 10, 4));
        l8.evil.add(new Level.UnitSpawn(Units.KOBOLD, 10, 3));
        l8.evil.add(new Level.UnitSpawn(Units.SKELETON, 9, 4));
        l8.evil.add(new Level.UnitSpawn(Units.SKELETON, 10, 5));
        l8.evil.add(new Level.UnitSpawn(Units.SKELETON, 10, 2));
        l8.evil.add(new Level.UnitSpawn(Units.SKELETON, 9, 2));
        l8.evil.add(new Level.UnitSpawn(Units.SKELETON, 10, 6));
        l8.evil.add(new Level.UnitSpawn(Units.SKELETON, 9, 3));
        l8.evil.add(new Level.UnitSpawn(Units.KOBOLD, 9, 5));
        ALL.add(l8);

        // #9 THE RUN
        Level l9 = new Level(96, 17, new String[]{"## THE RUN", "", "The skeleton attacks are", "intensifying. We need to", "investigate and find cause!", "", "The road out of the village", "is dangerous. Luckly,", "one of the Slug people", "accepted to aid us", "with the run!"}, () -> Game.unitInTargetArea());
        l9.good.add(new Level.UnitSpawn(Units.ELF, 1, 0));
        l9.good.add(new Level.UnitSpawn(Units.CAPTAIN, 0, 0));
        l9.good.add(new Level.UnitSpawn(Units.MAGE, 1, 3));
        l9.good.add(new Level.UnitSpawn(Units.SOLDIER, 8, 7));
        l9.good.add(new Level.UnitSpawn(Units.SLUGDO, 0, 1));
        l9.good.add(new Level.UnitSpawn(Units.SOLDIER, 7, 7));
        l9.evil.add(new Level.UnitSpawn(Units.SPIDER, 10, 3));
        l9.evil.add(new Level.UnitSpawn(Units.KOBOLD, 7, 0));
        l9.evil.add(new Level.UnitSpawn(Units.KOBOLD, 8, 0));
        l9.evil.add(new Level.UnitSpawn(Units.SNAKE, 6, 3));
        l9.evil.add(new Level.UnitSpawn(Units.SNAKE, 7, 3));
        l9.evil.add(new Level.UnitSpawn(Units.SNAKE, 6, 4));
        l9.evil.add(new Level.UnitSpawn(Units.SNAKE, 5, 1));
        l9.evil.add(new Level.UnitSpawn(Units.SKELETON, 1, 7));
        l9.evil.add(new Level.UnitSpawn(Units.RAT, 2, 7));
        l9.evil.add(new Level.UnitSpawn(Units.SKELETON, 0, 7));
        l9.evil.add(new Level.UnitSpawn(Units.SKELETON, 1, 6));
        l9.evil.add(new Level.UnitSpawn(Units.SNAKE, 0, 6));
        l9.evil.add(new Level.UnitSpawn(Units.RAT, 1, 5));
        l9.evil.add(new Level.UnitSpawn(Units.RAT, 0, 5));
        ALL.add(l9);

        // #10 THE LICHKING
        Level l10 = new Level(108, 17, new String[]{"## THE LICHKING", "", "Here's the source of all", "those skeletons! A Lichking", "has arrived to the village!", "", "Be quick to take it down as", "he can summon more skeletons!"}, Game::noMoreEnemies);
        l10.good.add(new Level.UnitSpawn(Units.ELF, 1, 3));
        l10.good.add(new Level.UnitSpawn(Units.CAPTAIN, 0, 4));
        l10.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 4));
        l10.good.add(new Level.UnitSpawn(Units.SLUGDO, 0, 3));
        l10.good.add(new Level.UnitSpawn(Units.SOLDIER, 0, 5));
        l10.evil.add(new Level.UnitSpawn(Units.LICHKING, 9, 4));
        l10.evil.add(new Level.UnitSpawn(Units.SKELETON, 9, 3));
        l10.evil.add(new Level.UnitSpawn(Units.SKELETON, 9, 5));
        ALL.add(l10);

        // #11 BRUTE ENCOUNTER
        Level l11 = new Level(120, 17, new String[]{"## BRUTE ENCOUNTER", "", "On the way back to the", "village, the fellowship has", "to cross a mountain pass.", "", "Brutes inhabit the area,", "Be careful as they're pretty", "tough!", "", "A solitary monk living there", "can provide some help!"}, Game::noMoreEnemies);
        l11.good.add(new Level.UnitSpawn(Units.ELF, 1, 3));
        l11.good.add(new Level.UnitSpawn(Units.CAPTAIN, 0, 4));
        l11.good.add(new Level.UnitSpawn(Units.MONK, 1, 1));
        l11.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 4));
        l11.good.add(new Level.UnitSpawn(Units.SOLDIER, 0, 5));
        l11.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 4));
        l11.evil.add(new Level.UnitSpawn(Units.BRUTE, 9, 3));
        l11.evil.add(new Level.UnitSpawn(Units.BRUTE, 10, 5));
        l11.evil.add(new Level.UnitSpawn(Units.BRUTE, 10, 4));
        ALL.add(l11);

        // #12 UH OH!
        Level l12 = new Level(132, 17, new String[]{"## UH OH!", "", "A couple of Kobolds ambushed", "your team! And some other", "thugs are joining the party!", "", "This is a tough one, plan", "carefully and exploit the", "wizard!"}, Game::noMoreEnemies);
        l12.good.add(new Level.UnitSpawn(Units.MAGE, 1, 3));
        l12.good.add(new Level.UnitSpawn(Units.SLUGDO, 0, 4));
        l12.good.add(new Level.UnitSpawn(Units.MONK, 0, 3));
        l12.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 4));
        l12.good.add(new Level.UnitSpawn(Units.SOLDIER, 0, 5));
        l12.evil.add(new Level.UnitSpawn(Units.KOBOLD, 8, 4));
        l12.evil.add(new Level.UnitSpawn(Units.KOBOLD, 8, 3));
        l12.evil.add(new Level.UnitSpawn(Units.GOBLIN, 10, 5));
        l12.evil.add(new Level.UnitSpawn(Units.GOBLIN, 10, 4));
        l12.evil.add(new Level.UnitSpawn(Units.BRUTE, 10, 1));
        l12.evil.add(new Level.UnitSpawn(Units.BRUTE, 7, 7));
        ALL.add(l12);

        // #13 DWARF RESCUE
        Level l13 = new Level(144, 17, new String[]{"## DWARF RESQUE", "", "A dwarf has been attacked", "by a bunch of Kobolds!", "", "Perhaps if we resque him", "he may help us later.", "", "Try to save him in the first", "turns as he's indispensable", "to defeat all those enemies!"}, Game::noMoreEnemies);
        l13.good.add(new Level.UnitSpawn(Units.MAGE, 1, 2));
        l13.good.add(new Level.UnitSpawn(Units.SLUGDO, 2, 2));
        l13.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 3));
        l13.good.add(new Level.UnitSpawn(Units.SOLDIER, 0, 4));
        l13.good.add(new Level.UnitSpawn(Units.DWARF, 7, 4));
        l13.evil.add(new Level.UnitSpawn(Units.KOBOLD, 8, 4));
        l13.evil.add(new Level.UnitSpawn(Units.KOBOLD, 7, 3));
        l13.evil.add(new Level.UnitSpawn(Units.KOBOLD, 7, 5));
        l13.evil.add(new Level.UnitSpawn(Units.KOBOLD, 10, 5));
        l13.evil.add(new Level.UnitSpawn(Units.GOBLIN, 10, 4));
        ALL.add(l13);

        // #14 DWARFS VS BRUTES
        Level l14 = new Level(156, 17, new String[]{"## DWARFS VS BRUTES", "", "Take advantage of the", "bottleneck to show them", "brutes who's the boss", "of the mountains!", "", "Also, BOMBS!"}, Game::noMoreEnemies);
        l14.good.add(new Level.UnitSpawn(Units.ELF, 2, 2));
        l14.good.add(new Level.UnitSpawn(Units.CAPTAIN, 1, 2));
        l14.good.add(new Level.UnitSpawn(Units.DWARF, 2, 3));
        l14.good.add(new Level.UnitSpawn(Units.BOMBER, 1, 4));
        l14.evil.add(new Level.UnitSpawn(Units.KOBOLD, 6, 4));
        l14.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 4));
        l14.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 3));
        l14.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 5));
        l14.evil.add(new Level.UnitSpawn(Units.BRUTE, 6, 5));
        l14.evil.add(new Level.UnitSpawn(Units.BRUTE, 10, 3));
        l14.evil.add(new Level.UnitSpawn(Units.GOBLIN, 10, 4));
        ALL.add(l14);

        {
            // #15 UNDER ATTACK
            Level l15 = new Level(168, 17, new String[]{"## UNDER ATTACK", "", "The tables have turned:", "now WE are under attack", "from ranged enemies!", "", "No subtleties here, just", "rush onto them!"}, Game::noMoreEnemies);
            l15.good.add(new Level.UnitSpawn(Units.DWARF, 1, 2));
            l15.good.add(new Level.UnitSpawn(Units.DWARF, 1, 3));
            l15.good.add(new Level.UnitSpawn(Units.SLUGDO, 2, 2));
            l15.good.add(new Level.UnitSpawn(Units.DWARF, 1, 4));
            l15.evil.add(new Level.UnitSpawn(Units.KOBOLD, 9, 4));
            l15.evil.add(new Level.UnitSpawn(Units.KOBOLD, 8, 4));
            l15.evil.add(new Level.UnitSpawn(Units.KOBOLD, 9, 3));
            ALL.add(l15);
            }
        {

            Level l15 = new Level(132, 26, new String[]{
            		"## ROCK AND ROLL", "", 
            		"There's a gorge full of enemies!",
            		"We are outnumbered! Our only hope",
            		"is a Stonecaster, one of those",
            		"strange magical living rocks", 
            		"that are said to be able to",
            		"control stone itself!","",
            		"Let's try and block their",
            		"advancement!"}, Game::noMoreEnemies);
            l15.good.add(new Level.UnitSpawn(Units.STONECASTER, 0, 2));
            l15.good.add(new Level.UnitSpawn(Units.ELF, 1, 3));
            l15.good.add(new Level.UnitSpawn(Units.ELF, 2, 2));
            l15.good.add(new Level.UnitSpawn(Units.CAPTAIN, 1, 4));
            l15.good.add(new Level.UnitSpawn(Units.SLUGDO, 2, 3));
            
            l15.evil.add(new Level.UnitSpawn(Units.GOBLIN, 9, 4));
            l15.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 4));
            l15.evil.add(new Level.UnitSpawn(Units.BRUTE, 9, 3));
            l15.evil.add(new Level.UnitSpawn(Units.RAT, 9, 2));
            l15.evil.add(new Level.UnitSpawn(Units.RAT, 8, 2));
            l15.evil.add(new Level.UnitSpawn(Units.BRUTE, 7, 7));
            ALL.add(l15);
            }
        {

            Level l15 = new Level(144, 26, new String[]{
            		"## BLOODTHIRST", "", 
            		"We're amost back at the village.",
            		"But in this land there's an ancient",
            		"graveyard..",
            		"Be careful, there's no telling what",
            		"kind of monsters you can find."
            		}, Game::noMoreEnemies);
            
            ALL.add(l15);
            }
            
        // #16 BACK AT THE VILLAGE
        Level l16 = new Level(180, 17, new String[]{"## BACK AT THE VILLAGE", "", "Oh no! The village has been", "Overrun by the forces of evil!"}, Game::noMoreEnemies);
        l16.good.add(new Level.UnitSpawn(Units.SOLDIER, 2, 2));
        l16.good.add(new Level.UnitSpawn(Units.SOLDIER, 2, 3));
        l16.good.add(new Level.UnitSpawn(Units.SLUGDO, 3, 2));
        l16.good.add(new Level.UnitSpawn(Units.ELF, 0, 3));
        l16.good.add(new Level.UnitSpawn(Units.CAPTAIN, 0, 4));
        l16.good.add(new Level.UnitSpawn(Units.DWARF, 2, 4));
        l16.good.add(new Level.UnitSpawn(Units.BOMBER, 1, 4));
        l16.good.add(new Level.UnitSpawn(Units.MAGE, 1, 3));
        l16.good.add(new Level.UnitSpawn(Units.DRUID, 1, 2));
        l16.evil.add(new Level.UnitSpawn(Units.SKELETON, 9, 5));
        l16.evil.add(new Level.UnitSpawn(Units.SKELETON, 8, 5));
        l16.evil.add(new Level.UnitSpawn(Units.KOBOLD, 9, 3));
        l16.evil.add(new Level.UnitSpawn(Units.KOBOLD, 10, 2));
        l16.evil.add(new Level.UnitSpawn(Units.LICHKING, 10, 3));
        l16.evil.add(new Level.UnitSpawn(Units.LICHKING, 10, 4));
        l16.evil.add(new Level.UnitSpawn(Units.GOBLIN, 10, 5));
        l16.evil.add(new Level.UnitSpawn(Units.KOBOLD, 10, 6));
        ALL.add(l16);

        // #17 SEARCHING FOR HELP
        Level l17 = new Level(192, 17, new String[]{"## SEARCHING FOR HELP", "", "The village destroyed, you", "have to rally some forces..", "You try and head for a", "nearby monastry, where a", "different kind of monks", "dwells. The fighting kind!", "", "Beat them to earn their", "respect and loyalty!"}, Game::noMoreEnemies);
        l17.good.add(new Level.UnitSpawn(Units.SOLDIER, 2, 2));
        l17.good.add(new Level.UnitSpawn(Units.DWARF, 2, 4));
        l17.good.add(new Level.UnitSpawn(Units.MONK, 1, 4));
        l17.good.add(new Level.UnitSpawn(Units.MAGE, 1, 3));
        l17.evil.add(new Level.UnitSpawn(Units.SHAOLIN, 7, 4));
        l17.evil.add(new Level.UnitSpawn(Units.SHAOLIN, 8, 3));
        l17.evil.add(new Level.UnitSpawn(Units.SHAOLIN, 9, 5));
        l17.evil.add(new Level.UnitSpawn(Units.SHAOLIN, 8, 5));
        ALL.add(l17);

        // #18 SURROUNDED BY BRUTES
        Level l18 = new Level(204, 17, new String[]{"## SURROUNDED BY BRUTES", "", "Wow, those kids were fast", "as lighting! Good that we", "won their respect!", "", "Looks like some brutes are", "trying to surround us.", "Teach em a lesson."}, Game::noMoreEnemies);
        l18.good.add(new Level.UnitSpawn(Units.SOLDIER, 4, 2));
        l18.good.add(new Level.UnitSpawn(Units.SHAOLIN, 5, 4));
        l18.good.add(new Level.UnitSpawn(Units.SHAOLIN, 6, 4));
        l18.good.add(new Level.UnitSpawn(Units.MAGE, 5, 3));
        l18.good.add(new Level.UnitSpawn(Units.MONK, 5, 5));
        l18.evil.add(new Level.UnitSpawn(Units.BRUTE, 4, 0));
        l18.evil.add(new Level.UnitSpawn(Units.BRUTE, 9, 3));
        l18.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 4));
        l18.evil.add(new Level.UnitSpawn(Units.BRUTE, 9, 5));
        l18.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 6));
        l18.evil.add(new Level.UnitSpawn(Units.BRUTE, 0, 5));
        l18.evil.add(new Level.UnitSpawn(Units.BRUTE, 0, 6));
        ALL.add(l18);

        // #19 CYCLOPS
        Level l19 = new Level(216, 17, new String[]{"## CYCLOPS", "", "Uh oh, looks who's there..", "Those guys are pretty", "big and strong.", "", "Luckly the Shaolin are", "fast enought to sting and", "back off."}, Game::noMoreEnemies);
        l19.good.add(new Level.UnitSpawn(Units.SOLDIER, 4, 2));
        l19.good.add(new Level.UnitSpawn(Units.SHAOLIN, 5, 4));
        l19.good.add(new Level.UnitSpawn(Units.SHAOLIN, 6, 4));
        l19.good.add(new Level.UnitSpawn(Units.MAGE, 5, 3));
        l19.evil.add(new Level.UnitSpawn(Units.CYCLOP, 9, 3));
        l19.evil.add(new Level.UnitSpawn(Units.CYCLOP, 8, 4));
        l19.evil.add(new Level.UnitSpawn(Units.SNAKE, 8, 6));
        l19.evil.add(new Level.UnitSpawn(Units.SNAKE, 0, 5));
        l19.evil.add(new Level.UnitSpawn(Units.SNAKE, 0, 6));
        ALL.add(l19);

        // #20 CALL IN THE PALADIN
        Level l20 = new Level(228, 17, new String[]{"## CALL IN THE PALADIN", "", "We arrived at the remote", "retire of a powerfull paladin.", "", "If we can get past all those", "monsters, and if we can find", "him maybe we can hire him!"}, () -> Game.unitInTargetArea());
        l20.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 3));
        l20.good.add(new Level.UnitSpawn(Units.SHAOLIN, 1, 4));
        l20.good.add(new Level.UnitSpawn(Units.SLUGDO, 0, 4));
        l20.good.add(new Level.UnitSpawn(Units.BOMBER, 0, 3));
        l20.good.add(new Level.UnitSpawn(Units.ELF, 0, 2));
        l20.good.add(new Level.UnitSpawn(Units.MAGE, 0, 5));
        l20.good.add(new Level.UnitSpawn(Units.MONK, 1, 6));
        l20.evil.add(new Level.UnitSpawn(Units.CYCLOP, 9, 3));
        l20.evil.add(new Level.UnitSpawn(Units.CYCLOP, 8, 4));
        l20.evil.add(new Level.UnitSpawn(Units.SNAKE, 9, 6));
        l20.evil.add(new Level.UnitSpawn(Units.CYCLOP, 10, 6));
        l20.evil.add(new Level.UnitSpawn(Units.RAT, 7, 1));
        l20.evil.add(new Level.UnitSpawn(Units.RAT, 8, 1));
        l20.evil.add(new Level.UnitSpawn(Units.RAT, 8, 2));
        l20.evil.add(new Level.UnitSpawn(Units.RAT, 9, 5));
        l20.evil.add(new Level.UnitSpawn(Units.RAT, 10, 3));
        l20.evil.add(new Level.UnitSpawn(Units.SPIDER, 8, 3));
        l20.evil.add(new Level.UnitSpawn(Units.KOBOLD, 9, 4));
        ALL.add(l20);

        // #21 ROAD TO THE KING
        Level l21 = new Level(84, 26, new String[]{"## ROAD TO THE KING", "", "With the paladin on our side", "we'll be able to get the King", "attention and involvement!", "", "It's along journey to the", "castle tho, better start", "walking..."}, Game::noMoreEnemies);
        l21.good.add(new Level.UnitSpawn(Units.ELF, 0, 1));
        l21.good.add(new Level.UnitSpawn(Units.PALADIN, 1, 0));
        l21.good.add(new Level.UnitSpawn(Units.BOMBER, 0, 0));
        l21.evil.add(new Level.UnitSpawn(Units.SLIME, 10, 7));
        l21.traps.add(new ColumnTrap(2, () -> { 
        	Game.showDialog(List.of("", "\"Ew.. What's that thing?", ""), null, DialogType.POPUP);
        	return null;
        }));
        ALL.add(l21);

        // #22 EVEN MORE SLIME!
        Level l22 = new Level(96, 26, new String[]{"## EVEN MORE SLIME!", "", "Ew, those blobs were", "disgusting! And it looks like", "there's even more of them", "here! They spread like crazy!", "", "We must eradicate them asap", "or they'll grow out of", "control!", "", "We have few units, but we", "can use these litte doggies!"}, Game::noMoreEnemies);
        l22.good.add(new Level.UnitSpawn(Units.ELF, 0, 1));
        l22.good.add(new Level.UnitSpawn(Units.PALADIN, 1, 1));
        l22.good.add(new Level.UnitSpawn(Units.BOMBER, 1, 2));
        l22.good.add(new Level.UnitSpawn(Units.DWARF, 0, 2));
        l22.good.add(new Level.UnitSpawn(Units.DOG, 0, 3));
        l22.good.add(new Level.UnitSpawn(Units.DOG, 1, 3));
        l22.evil.add(new Level.UnitSpawn(Units.SLIME, 10, 2));
        l22.evil.add(new Level.UnitSpawn(Units.SLIME, 10, 4));
        l22.evil.add(new Level.UnitSpawn(Units.SLIME, 10, 5));
        l22.evil.add(new Level.UnitSpawn(Units.SLIME, 9, 3));
        ALL.add(l22);

        // #23 TENTACLES
        ALL.add(new Level(108, 26, new String[]{"## TENTACLES", "", "I'm glad we left those blobs", "behind.", "Now there's a dangerous river", "to cross, it's populated with", "wild river octopuses ready", "to attack!", "", "Be careful!"}, Game::noMoreEnemies));


        
        {
       	 Level l15 = new Level(156, 26, new String[]{
            		"## CENTAUR FOREST", "", 
            		"BLA."
            		}, Game::noMoreEnemies);
            
            ALL.add(l15);
       }
       
        {
       	 Level l15 = new Level(168, 26, new String[]{
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
            		}, Game::noMoreEnemies);
            
            ALL.add(l15);
       }
       
        // #24 ALMOST AT THE CASTLE
        Level l24 = new Level(120, 26, new String[]{"## ALMOST AT THE CASTLE", "", "We are in the vicinity of the", "King Castle! But first we have", "to pass the desolate lands of", "Kfnir, where a powerful god", "sleeps. Woe betide he who", "awakes him from his slumber!"}, Game::noMoreEnemies);
        l24.good.add(new Level.UnitSpawn(Units.PALADIN, 0, 1));
        l24.good.add(new Level.UnitSpawn(Units.SOLDIER, 0, 3));
        l24.good.add(new Level.UnitSpawn(Units.SLUGDO, 1, 2));
        l24.good.add(new Level.UnitSpawn(Units.ELF, 1, 1));
        l24.good.add(new Level.UnitSpawn(Units.ELF, 0, 4));
        l24.evil.add(new Level.UnitSpawn(Units.PDOR, 8, 3));
        l24.traps.add(new ColumnTrap(1, () -> {
            Game.showDialog(List.of("Pdor, son of Kmer:", "", "\"Who dares wake me up from", "my slumber??\""), () -> {
                Game.showDialog(List.of("Paladin:", "", "\"Wops!", "I think we are", "in trouble guys.\""), null, DialogType.POPUP);
            }, DialogType.POPUP);
            return null;
        }));
        ALL.add(l24);

        // #25 THE KING CASTLE
        Level l25 = new Level(0, 26, new String[]{
        		"#25 THE KING CASTLE", "", 
        		"We finally reached the castle!",
        		"With the paladin on our side", 
        		"we can ask for the king help!", "", 
        		"But wait! It looks like the", "castle is besieged by", "an army of monsters!", "", "Let's help our people!"}, Game::noMoreEnemies);
        l25.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 3));
        l25.good.add(new Level.UnitSpawn(Units.PALADIN, 1, 4));
        l25.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 5));
        l25.good.add(new Level.UnitSpawn(Units.DWARF, 0, 4));
        l25.good.add(new Level.UnitSpawn(Units.ELF, 1, 6));
        l25.evil.add(new Level.UnitSpawn(Units.GOBLIN, 9, 3));
        l25.evil.add(new Level.UnitSpawn(Units.GOBLIN, 8, 4));
        l25.evil.add(new Level.UnitSpawn(Units.KOBOLD, 8, 2));
        l25.evil.add(new Level.UnitSpawn(Units.KOBOLD, 10, 3));
        l25.evil.add(new Level.UnitSpawn(Units.RAT, 7, 1));
        l25.evil.add(new Level.UnitSpawn(Units.RAT, 8, 1));
        l25.evil.add(new Level.UnitSpawn(Units.SNAKE, 5, 7));
        l25.evil.add(new Level.UnitSpawn(Units.SNAKE, 4, 7));
        ALL.add(l25);

        // #26 COUNTERATTACK!
        Level l26 = new Level(12, 26, new String[]{"## COUNTERATTACK!", "", "The king agreed to meet us", "and decided it's time to", "fight the monsters back", "and free the reign once", "again!", "", "We must find where they", "come from!"}, Game::noMoreEnemies);
        l26.good.add(new Level.UnitSpawn(Units.KING, 1, 3));
        l26.good.add(new Level.UnitSpawn(Units.PALADIN, 1, 4));
        l26.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 5));
        l26.good.add(new Level.UnitSpawn(Units.SOLDIER, 0, 5));
        l26.good.add(new Level.UnitSpawn(Units.SOLDIER, 0, 6));
        l26.good.add(new Level.UnitSpawn(Units.MONK, 0, 3));
        l26.evil.add(new Level.UnitSpawn(Units.CYCLOP, 9, 3));
        l26.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 4));
        l26.evil.add(new Level.UnitSpawn(Units.CYCLOP, 8, 2));
        l26.evil.add(new Level.UnitSpawn(Units.BRUTE, 10, 3));
        l26.evil.add(new Level.UnitSpawn(Units.BRUTE, 9, 4));
        l26.traps.add(new ColumnTrap(2, () -> { 
        	Game.showDialog(List.of("", "\"Wow, having the king", "on the battlefield", "gives a huge morale", "boost to our troops!", "", "We are unstoppable!\"", ""), null, DialogType.POPUP);
        	return null;
        }));
        ALL.add(l26);

        // #27 THE WITCH
        Level l27 = new Level(24, 26, new String[]{"## THE WITCH", "", "We need some help to locate", "The source of the evilness", "that's ruining our land.", "", "A famous witch lives nearby", "in a dilapidated house.", "Perhaps her magic can", "help us!"}, () -> Game.unitInTargetArea());
        l27.good.add(new Level.UnitSpawn(Units.KING, 1, 3));
        l27.good.add(new Level.UnitSpawn(Units.PALADIN, 1, 2));
        l27.good.add(new Level.UnitSpawn(Units.SOLDIER, 0, 2));
        l27.good.add(new Level.UnitSpawn(Units.MONK, 0, 3));
        l27.evil.add(new Level.UnitSpawn(Units.KOBOLD, 6, 1));
        l27.evil.add(new Level.UnitSpawn(Units.KOBOLD, 7, 1));
        l27.evil.add(new Level.UnitSpawn(Units.KOBOLD, 2, 7));
        l27.evil.add(new Level.UnitSpawn(Units.KOBOLD, 3, 7));
        l27.evil.add(new Level.UnitSpawn(Units.KOBOLD, 5, 7));
        l27.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 4));
        l27.evil.add(new Level.UnitSpawn(Units.BRUTE, 9, 5));
        l27.evil.add(new Level.UnitSpawn(Units.RAT, 8, 3));
        l27.evil.add(new Level.UnitSpawn(Units.RAT, 9, 3));
        l27.evil.add(new Level.UnitSpawn(Units.SNAKE, 6, 3));
        l27.evil.add(new Level.UnitSpawn(Units.SNAKE, 6, 4));
        ALL.add(l27);

        // #28 WITCHY WITCH
        Level l28 = new Level(36, 26, new String[]{"## WITCHY WITCH", "", "The witch joins the battle.", "Sure she'll be useful, but", "you're not sure you like her.", "", "She need to reach a magical", "well where she can perform a", "divination and locate the", "monsters source.", "", "We're kind of surrounded here,", "Power up those elves and", "resist the assault!"}, Game::noMoreEnemies);
        l28.good.add(new Level.UnitSpawn(Units.KING, 0, 0));
        l28.good.add(new Level.UnitSpawn(Units.ELF, 1, 0));
        l28.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 2));
        l28.good.add(new Level.UnitSpawn(Units.ELF, 0, 2));
        l28.good.add(new Level.UnitSpawn(Units.CAPTAIN, 1, 1));
        l28.good.add(new Level.UnitSpawn(Units.WITCH, 0, 1));
        l28.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 4));
        l28.evil.add(new Level.UnitSpawn(Units.BRUTE, 9, 5));
        l28.evil.add(new Level.UnitSpawn(Units.BRUTE, 10, 5));
        l28.evil.add(new Level.UnitSpawn(Units.BRUTE, 6, 2));
        l28.evil.add(new Level.UnitSpawn(Units.RAT, 8, 3));
        l28.evil.add(new Level.UnitSpawn(Units.RAT, 7, 7));
        l28.evil.add(new Level.UnitSpawn(Units.CYCLOP, 9, 3));
        l28.evil.add(new Level.UnitSpawn(Units.GOBLIN, 7, 3));
        l28.evil.add(new Level.UnitSpawn(Units.GOBLIN, 5, 6));
        l28.evil.add(new Level.UnitSpawn(Units.SNAKE, 7, 4));
        ALL.add(l28);

        // #29 THE MAGICAL WELL
        Level l29 = new Level(48, 26, new String[]{"## THE MAGICAL WELL", "", "We reached the well, move", "the witch nearby to perform", "the ritual!", "", "Looks like the forces of", "Evil got wind of our plans", "becouse monsters are", "literally pouring in!"}, 
        		() -> false // win is determined by tiletrap
        		);
        l29.good.add(new Level.UnitSpawn(Units.KING, 0, 0));
        l29.good.add(new Level.UnitSpawn(Units.ELF, 0, 3));
        l29.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 2));
        l29.good.add(new Level.UnitSpawn(Units.ELF, 0, 2));
        l29.good.add(new Level.UnitSpawn(Units.CAPTAIN, 1, 1));
        l29.good.add(new Level.UnitSpawn(Units.WITCH, 0, 1));
        l29.evil.add(new Level.UnitSpawn(Units.BRUTE, 8, 7));
        l29.turner = () -> {
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
        };
        l29.traps.add(new TileTrap(7, 4, () -> {
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
        }));
        ALL.add(l29);

        // #30 THE PORTAL
        Level l30 = new Level(60, 26, new String[]{"## THE PORTAL", "", "This is it, the final battle!", "", "The portal is here, and it has", "just spit out the strongest", "of all enemies, the Archdemon!", "", "We must kill all monsters and", "destroy the portal! While it is", "open, more skeletons will", "enter!", "", "Good luck, for our freedom!"}, Game::noMoreEnemies);
        l30.good.add(new Level.UnitSpawn(Units.KING, 0, 0));
        l30.good.add(new Level.UnitSpawn(Units.ELF, 0, 3));
        l30.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 2));
        l30.good.add(new Level.UnitSpawn(Units.ELF, 0, 2));
        l30.good.add(new Level.UnitSpawn(Units.CAPTAIN, 1, 1));
        l30.good.add(new Level.UnitSpawn(Units.WITCH, 0, 1));
        l30.good.add(new Level.UnitSpawn(Units.PALADIN, 0, 4));
        l30.good.add(new Level.UnitSpawn(Units.BOMBER, 0, 5));
        l30.good.add(new Level.UnitSpawn(Units.SLUGDO, 2, 0));
        l30.good.add(new Level.UnitSpawn(Units.DWARF, 1, 0));
        l30.good.add(new Level.UnitSpawn(Units.MAGE, 1, 4));
        l30.good.add(new Level.UnitSpawn(Units.DRUID, 1, 3));
        l30.evil.add(new Level.UnitSpawn(Units.ARCHDEMON, 10, 7));
        l30.evil.add(new Level.UnitSpawn(Units.LICHKING, 10, 6));
        l30.evil.add(new Level.UnitSpawn(Units.CYCLOP, 9, 7));
        l30.evil.add(new Level.UnitSpawn(Units.KOBOLD, 8, 6));
        l30.evil.add(new Level.UnitSpawn(Units.KOBOLD, 9, 5));
        l30.evil.add(new Level.UnitSpawn(Units.BRUTE, 6, 4));
        l30.evil.add(new Level.UnitSpawn(Units.BRUTE, 5, 5));
        l30.evil.add(new Level.UnitSpawn(Units.GOBLIN, 9, 3));
        l30.evil.add(new Level.UnitSpawn(Units.PORTAL, 9, 6));
        l30.turner = () -> {
            if (Game.count(Units.PORTAL) > 0 && Game.count(Units.SKELETON) < 10) {
                /*Unit u1 = Game.addNear(Units.SKELETON, 10, 5, 1);
                if (u1 != null) u1.eff.put(Effects.SPENT, 1);
                Unit u2 = Game.addNear(Units.SKELETON, 4, 7, 1);
                if (u2 != null) u2.eff.put(Effects.SPENT, 1);
                Unit u3 = Game.addNear(Units.SKELETON, 5, 0, 1);
                if (u3 != null) u3.eff.put(Effects.SPENT, 1);
                */
                
                
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
        };
        ALL.add(l30);

        // #31 VICTORY!
        Level l31 = new Level(72, 26, new String[]{"## VICTORY!", "", "The portal has been closed!", "We WON the war against the", "evil forces!", "", "Once the dust settled, the", "celebrations concluded, ", "peace finally return to the", "lands."}, () -> false);
        l31.good.add(new Level.UnitSpawn(Units.KING, 0, 0));
        l31.good.add(new Level.UnitSpawn(Units.FARMER, 5, 6));
        l31.good.add(new Level.UnitSpawn(Units.FARMER, 6, 4));
        l31.good.add(new Level.UnitSpawn(Units.FARMER, 7, 2));
        l31.good.add(new Level.UnitSpawn(Units.SOLDIER, 0, 2));
        l31.good.add(new Level.UnitSpawn(Units.SOLDIER, 1, 2));
        l31.turner = () -> {
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
        };
        l31.traps.add(new TileTrap(10, 5, () -> {
        	
        	
            Game.showDialog(List.of("Uhm","", "Hey, the road continues", "down there!", "", "Uhuh let's see where it takes.."), Game::doWin, DialogType.POPUP);
            
            return null;
        }));
        ALL.add(l31);
        
        
        Level sec = new Level(180, 26, new String[]{"## LOL Secrare et Level!", "", "Good, good.", "You found the secret level", "you smart person!", "", "Have fun!"}, () -> false);
        sec.traps.add(new TileTrap(10,6, () -> {
        	
        	
            Game.showDialog(List.of("Hey!","", "The road continues again..", "", "Another secret level??"), Game::doWin, DialogType.POPUP);
            
            return null;
        }));
       
        ALL.add(sec);

        Level sec2 = new Level(192, 26, new String[]{"## ROGER AGAINST ALL!", "", "Another secret level :D", "", "Roger is a chonky cat", "his power is beyond human", "comprehension.", "", "This is the last secret level, ok? :D", "Have fun!"}, () -> false);
        
        ALL.add(sec2);

        
        int i = 0;
        for (Level level : ALL) {
        	i++;
			level.intro[0] = level.intro[0].replace("##", "#"+i);
		}
        
    }
}