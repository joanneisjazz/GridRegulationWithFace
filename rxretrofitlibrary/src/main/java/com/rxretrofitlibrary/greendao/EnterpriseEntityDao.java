package com.rxretrofitlibrary.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.EnterpriseEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ENTERPRISE_ENTITY".
*/
public class EnterpriseEntityDao extends AbstractDao<EnterpriseEntity, String> {

    public static final String TABLENAME = "ENTERPRISE_ENTITY";

    /**
     * Properties of entity EnterpriseEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Nature = new Property(2, String.class, "nature", false, "NATURE");
        public final static Property Code = new Property(3, String.class, "code", false, "CODE");
        public final static Property UnifiedCode = new Property(4, String.class, "unifiedCode", false, "UNIFIED_CODE");
        public final static Property LegalName = new Property(5, String.class, "legalName", false, "LEGAL_NAME");
        public final static Property RegisteredCapital = new Property(6, String.class, "registeredCapital", false, "REGISTERED_CAPITAL");
    }


    public EnterpriseEntityDao(DaoConfig config) {
        super(config);
    }
    
    public EnterpriseEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ENTERPRISE_ENTITY\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"NATURE\" TEXT," + // 2: nature
                "\"CODE\" TEXT," + // 3: code
                "\"UNIFIED_CODE\" TEXT," + // 4: unifiedCode
                "\"LEGAL_NAME\" TEXT," + // 5: legalName
                "\"REGISTERED_CAPITAL\" TEXT);"); // 6: registeredCapital
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ENTERPRISE_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EnterpriseEntity entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String nature = entity.getNature();
        if (nature != null) {
            stmt.bindString(3, nature);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(4, code);
        }
 
        String unifiedCode = entity.getUnifiedCode();
        if (unifiedCode != null) {
            stmt.bindString(5, unifiedCode);
        }
 
        String legalName = entity.getLegalName();
        if (legalName != null) {
            stmt.bindString(6, legalName);
        }
 
        String registeredCapital = entity.getRegisteredCapital();
        if (registeredCapital != null) {
            stmt.bindString(7, registeredCapital);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EnterpriseEntity entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String nature = entity.getNature();
        if (nature != null) {
            stmt.bindString(3, nature);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(4, code);
        }
 
        String unifiedCode = entity.getUnifiedCode();
        if (unifiedCode != null) {
            stmt.bindString(5, unifiedCode);
        }
 
        String legalName = entity.getLegalName();
        if (legalName != null) {
            stmt.bindString(6, legalName);
        }
 
        String registeredCapital = entity.getRegisteredCapital();
        if (registeredCapital != null) {
            stmt.bindString(7, registeredCapital);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public EnterpriseEntity readEntity(Cursor cursor, int offset) {
        EnterpriseEntity entity = new EnterpriseEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nature
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // code
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // unifiedCode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // legalName
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // registeredCapital
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EnterpriseEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNature(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCode(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUnifiedCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLegalName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRegisteredCapital(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final String updateKeyAfterInsert(EnterpriseEntity entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(EnterpriseEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(EnterpriseEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
