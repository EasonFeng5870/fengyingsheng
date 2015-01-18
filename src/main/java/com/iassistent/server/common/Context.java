package com.iassistent.server.common;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class Context implements Serializable {

    private static final long serialVersionUID	= -2668669793147105484L;

    private static final ThreadLocal<Context> s_context = new ThreadLocal<Context>();

    public static final String					CONTEXT_ID			= "ias-context-id";
    public static final String					CONTEXT_NAME		= "ias-context";

    private JSONObject f_internal;

    /**
     * Private constructor. Those who wish to obtain a new Context instance
     * should use the static methods newContext() and newContext( String )
     */
    private Context( JSONObject obj )
    {
        f_internal = obj;
    }

    public static Context getCurrentContext(){
        return s_context.get();
    }

    public boolean has(String key){
        return f_internal.has(key);
    }

    public String getString( String key )
    {
        return f_internal.getString(key);
    }

    /**
     * Set the actual object passed in as the current context. Note that this
     * will *replace* the current context object, so anybody that has a Context
     * object locally will no longer have changes they make to that object
     * actually reflected in the current context! This should be taken into
     * account when using this method. See {@link #modifyCurrentContext(String)}
     * for a mechanism to change the current context without changing the actual
     * object reference.
     *
     * @param context
     */
    public static void setCurrentContext( Context context )
    {
        s_context.set(context);
    }

    public static JSONObject newJSONObject(){
        return  new JSONObject();
    }

    public static JSONObject fromString( String str )
    {
        return new JSONObject(str);
    }

    /**
     * Modify the current context object (and create one if it doesn't exist) to
     * mimic the serialized form passed in. This method takes a String instead
     * of a Context object to make it clear that the subsequent changes made to
     * any locally cached Context object will not be reflected in the current
     * context.
     *
     * @param serialized
     * @return the current Context
     * @throws IllegalArgumentException
     *             if the serialized String passed in doesn't represent a
     *             serialized Context.
     */
    public static Context modifyCurrentContext( String serialized )
    {
        JSONObject newInternal;

        try
        {
            if(null == serialized || "".equals(serialized.trim())) {
                newInternal =  newJSONObject();
            }
            newInternal = fromString(serialized);
        }
        catch (Exception t)
        {
//			s_logger.performException("CM-0076-0001", "");
            throw new IllegalArgumentException("Invalid serialized context", t);
        }

        Context ctx = getSafeCurrentContext();

        ctx.f_internal = newInternal;

        return ctx;
    }

    /**
     * Same as getCurrentContext(), but will never return a null context. Note
     * that this method may have the side effect of setting the current context
     * (if it was previously null)
     *
     * @return
     */
    public static Context getSafeCurrentContext()
    {
        Context ctx = getCurrentContext();

        if (ctx == null)
        {
            ctx = newContext();
            setCurrentContext(ctx);
        }

        return ctx;
    }

    /**
     * Create a new, empty Context. Note that this *does not* affect the current
     * context. You must call setCurrentContext if you wish to do so.
     *
     * @return the new, empty Context
     */
    public static Context newContext()
    {
        return new Context(newJSONObject());
    }


    public Iterator<String> iterator()
    {
        return f_internal.keys();
    }

    @Override
    public String toString()
    {
        return String.valueOf(f_internal);
    }

}
