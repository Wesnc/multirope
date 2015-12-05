package info.rednecksolutions.wurmunlimited.multirope;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.ItemTemplatesCreatedListener;
import org.gotti.wurmunlimited.modloader.interfaces.ServerStartedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmMod;

import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;

import info.rednecksolutions.wurmunlimited.multirope.config.Constants;
import info.rednecksolutions.wurmunlimited.multirope.items.Multirope;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class MultiRope implements WurmMod, Configurable, Initable, ServerStartedListener, ItemTemplatesCreatedListener
{
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	Multirope rope = null;
	
	public void onItemTemplatesCreated()
	{
		if(Constants.MULTIROPE_ENABLED)
			rope = new Multirope();
	}
	
	@Override public void configure(Properties props) {
		Constants.MULTIROPE_ENABLED = Boolean.parseBoolean(props.getProperty(Constants.PROP_MULTIROPE_ENABLED));
		Constants.ANIMAL_LEADLIMIT = Integer.parseInt(props.getProperty(Constants.PROP_MULTIROPE_LEADLIMIT));
		Constants.ITEMWEIGHT_MULTIROPE = Integer.parseInt(props.getProperty(Constants.PROP_MULTIROPE_WEIGHT));
		
		logger.log(Level.INFO, "[MR] Mulitrope enabled: "+Constants.MULTIROPE_ENABLED);
		logger.log(Level.INFO, "[MR] Multirope animal lead limit: "+Constants.ANIMAL_LEADLIMIT);
		logger.log(Level.INFO, "[MR] Multirope item weight: "+Constants.ITEMWEIGHT_MULTIROPE);
	}
	
	public void init() 
	{ 
		try 
		{
			String desc = Descriptor.ofMethod(CtClass.booleanType, new CtClass[] {
				HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Action"),
				HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.Creature"),
				HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.Creature"),
				CtClass.shortType,
				CtClass.floatType
			});
			HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.CreatureBehaviour").getMethod("action", desc).instrument(new ExprEditor()
			{
				public void edit(MethodCall m) throws CannotCompileException
				{
					if("com.wurmonline.server.creatures.Creature".equals(m.getMethodName()))
					{
						m.replace("$_ = info.rednecksolutions.wurmunlimited.multirope.items.Multirope.mayLeadMoreCreatures(performer, source)");
					}
				}
			});
		} 
		catch (NotFoundException | CannotCompileException ex) { logger.log(Level.SEVERE, ex.getMessage(), ex.getStackTrace());}
	}
	
	public void onServerStarted() 
	{
		logger.log(Level.INFO, "Redneck Solutions MultiRope init.");
	}

	
}
