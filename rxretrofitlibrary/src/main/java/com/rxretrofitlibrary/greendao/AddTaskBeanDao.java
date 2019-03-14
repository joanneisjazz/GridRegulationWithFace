package com.rxretrofitlibrary.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.AddTaskBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ADD_TASK_BEAN".
*/
public class AddTaskBeanDao extends AbstractDao<AddTaskBean, String> {

    public static final String TABLENAME = "ADD_TASK_BEAN";

    /**
     * Properties of entity AddTaskBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Entid = new Property(0, String.class, "entid", true, "ENTID");
        public final static Property Entname = new Property(1, String.class, "entname", false, "ENTNAME");
        public final static Property Enttype = new Property(2, String.class, "enttype", false, "ENTTYPE");
        public final static Property Entregion = new Property(3, String.class, "entregion", false, "ENTREGION");
        public final static Property Entcredit = new Property(4, String.class, "entcredit", false, "ENTCREDIT");
        public final static Property Oisuper = new Property(5, String.class, "oisuper", false, "OISUPER");
        public final static Property Insptable = new Property(6, String.class, "insptable", false, "INSPTABLE");
        public final static Property CreateBy = new Property(7, String.class, "createBy", false, "CREATE_BY");
        public final static Property UpdateBy = new Property(8, String.class, "updateBy", false, "UPDATE_BY");
        public final static Property Inspstart = new Property(9, String.class, "inspstart", false, "INSPSTART");
    }


    public AddTaskBeanDao(DaoConfig config) {
        super(config);
    }
    
    public AddTaskBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ADD_TASK_BEAN\" (" + //
                "\"ENTID\" TEXT PRIMARY KEY NOT NULL ," + // 0: entid
                "\"ENTNAME\" TEXT," + // 1: entname
                "\"ENTTYPE\" TEXT," + // 2: enttype
                "\"ENTREGION\" TEXT," + // 3: entregion
                "\"ENTCREDIT\" TEXT," + // 4: entcredit
                "\"OISUPER\" TEXT," + // 5: oisuper
                "\"INSPTABLE\" TEXT," + // 6: insptable
                "\"CREATE_BY\" TEXT," + // 7: createBy
                "\"UPDATE_BY\" TEXT," + // 8: updateBy
                "\"INSPSTART\" TEXT);"); // 9: inspstart
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ADD_TASK_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AddTaskBean entity) {
        stmt.clearBindings();
 
        String entid = entity.getEntid();
        if (entid != null) {
            stmt.bindString(1, entid);
        }
 
        String entname = entity.getEntname();
        if (entname != null) {
            stmt.bindString(2, entname);
        }
 
        String enttype = entity.getEnttype();
        if (enttype != null) {
            stmt.bindString(3, enttype);
        }
 
        String entregion = entity.getEntregion();
        if (entregion != null) {
            stmt.bindString(4, entregion);
        }
 
        String entcredit = entity.getEntcredit();
        if (entcredit != null) {
            stmt.bindString(5, entcredit);
        }
 
        String oisuper = entity.getOisuper();
        if (oisuper != null) {
            stmt.bindString(6, oisuper);
        }
 
        String insptable = entity.getInsptable();
        if (insptable != null) {
            stmt.bindString(7, insptable);
        }
 
        String createBy = entity.getCreateBy();
        if (createBy != null) {
            stmt.bindString(8, createBy);
        }
 
        String updateBy = entity.getUpdateBy();
        if (updateBy != null) {
            stmt.bindString(9, updateBy);
        }
 
        String inspstart = entity.getInspstart();
        if (inspstart != null) {
            stmt.bindString(10, inspstart);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AddTaskBean entity) {
        stmt.clearBindings();
 
        String entid = entity.getEntid();
        if (entid != null) {
            stmt.bindString(1, entid);
        }
 
        String entname = entity.getEntname();
        if (entname != null) {
            stmt.bindString(2, entname);
        }
 
        String enttype = entity.getEnttype();
        if (enttype != null) {
            stmt.bindString(3, enttype);
        }
 
        String entregion = entity.getEntregion();
        if (entregion != null) {
            stmt.bindString(4, entregion);
        }
 
        String entcredit = entity.getEntcredit();
        if (entcredit != null) {
            stmt.bindString(5, entcredit);
        }
 
        String oisuper = entity.getOisuper();
        if (oisuper != null) {
            stmt.bindString(6, oisuper);
        }
 
        String insptable = entity.getInsptable();
        if (insptable != null) {
            stmt.bindString(7, insptable);
        }
 
        String createBy = entity.getCreateBy();
        if (createBy != null) {
            stmt.bindString(8, createBy);
        }
 
        String updateBy = entity.getUpdateBy();
        if (updateBy != null) {
            stmt.bindString(9, updateBy);
        }
 
        String inspstart = entity.getInspstart();
        if (inspstart != null) {
            stmt.bindString(10, inspstart);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public AddTaskBean readEntity(Cursor cursor, int offset) {
        AddTaskBean entity = new AddTaskBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // entid
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // entname
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // enttype
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // entregion
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // entcredit
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // oisuper
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // insptable
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // createBy
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // updateBy
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // inspstart
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AddTaskBean entity, int offset) {
        entity.setEntid(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setEntname(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setEnttype(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setEntregion(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEntcredit(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setOisuper(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setInsptable(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCreateBy(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUpdateBy(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setInspstart(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final String updateKeyAfterInsert(AddTaskBean entity, long rowId) {
        return entity.getEntid();
    }
    
    @Override
    public String getKey(AddTaskBean entity) {
        if(entity != null) {
            return entity.getEntid();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AddTaskBean entity) {
        return entity.getEntid() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
