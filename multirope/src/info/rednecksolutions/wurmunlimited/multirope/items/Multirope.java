package info.rednecksolutions.wurmunlimited.multirope.items;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.CreationCategories;
import com.wurmonline.server.items.CreationEntry;
import com.wurmonline.server.items.CreationEntryCreator;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.skills.SkillList;

import info.rednecksolutions.wurmunlimited.multirope.config.Constants;

public class Multirope 
{
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	public Multirope() 
	{
		logger.log(Level.INFO, "[MR] Multirope enabled, creating items...");
		try
		{
			createItem();
		}
		catch(IOException ex) { logger.log(Level.SEVERE, ex.getMessage(), ex.getStackTrace());}
		catch(Exception ex) { logger.log(Level.SEVERE, ex.getMessage(), ex.getStackTrace());}
	}
	
	private void createItem() throws IOException, Exception
	{
		ItemTemplateBuilder bmr = new ItemTemplateBuilder("wesnc.multirope");
		bmr.name("leading rope", "ropes", Constants.ITEMDESCRIPTION_MULTIROPE);
		bmr.descriptions("excellent", "good", "ok", "poor");
		bmr.itemTypes(new short[] { 46, 64, 147, 189 });
		bmr.imageNumber((short) 621);
		bmr.behaviourType((short) 1);
		bmr.combatDamage(0);
		bmr.decayTime(Constants.ITEMDECAYTIME_MULTIROPE);
		bmr.dimensions(1, 10, 20);
		bmr.primarySkill(-10);
		bmr.bodySpaces(MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY);
		bmr.modelName(Constants.MAPPING_MULTIROPE);
		bmr.difficulty(Constants.ITEMDIFF_MULTIROPE);
		bmr.weightGrams(Constants.ITEMWEIGHT_MULTIROPE);
		ItemTemplate templateMR = bmr.build();
		Constants.ID_MULTIROPE = templateMR.getTemplateId();
		createCreationEntry(Constants.ID_MULTIROPE);
		
	}
	
	private void createCreationEntry(int id) throws Exception
	{
		if(id > 0)
		{
			logger.log(Level.INFO, "[MR] Creating creation entry for: "+id);
			CreationEntry entry = CreationEntryCreator.createSimpleEntry(SkillList.ROPEMAKING, ItemList.ropeTool, ItemList.wempFibre, Constants.ID_MULTIROPE, false, true, 30.0f, false, false, CreationCategories.ROPES);
		}
		else
			throw new Exception("ERROR ATTEMPTING TO CREATE ENTRY FOR "+id);
	}
	
	public static boolean mayLeadMoreCreatures(Creature performer, Item source)
	{
		return performer.mayLeadMoreCreatures() || source.getTemplateId() == Constants.ID_MULTIROPE && performer.getFollowers().length < Constants.ANIMAL_LEADLIMIT;
	}

}
