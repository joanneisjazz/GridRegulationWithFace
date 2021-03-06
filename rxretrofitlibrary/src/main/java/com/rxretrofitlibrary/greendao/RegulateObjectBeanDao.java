package com.rxretrofitlibrary.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateObjectBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "REGULATE_OBJECT_BEAN".
*/
public class RegulateObjectBeanDao extends AbstractDao<RegulateObjectBean, String> {

    public static final String TABLENAME = "REGULATE_OBJECT_BEAN";

    /**
     * Properties of entity RegulateObjectBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Nature = new Property(2, String.class, "nature", false, "NATURE");
        public final static Property Code = new Property(3, String.class, "code", false, "CODE");
        public final static Property UnifiedCode = new Property(4, String.class, "unifiedCode", false, "UNIFIED_CODE");
        public final static Property BelongedTrade = new Property(5, String.class, "belongedTrade", false, "BELONGED_TRADE");
        public final static Property Address = new Property(6, String.class, "address", false, "ADDRESS");
        public final static Property Longitude = new Property(7, double.class, "longitude", false, "LONGITUDE");
        public final static Property Latitude = new Property(8, double.class, "latitude", false, "LATITUDE");
        public final static Property ContactPhone = new Property(9, String.class, "contactPhone", false, "CONTACT_PHONE");
        public final static Property Inspcount = new Property(10, String.class, "inspcount", false, "INSPCOUNT");
        public final static Property Entcredit = new Property(11, String.class, "entcredit", false, "ENTCREDIT");
        public final static Property Passrate = new Property(12, String.class, "passrate", false, "PASSRATE");
        public final static Property Entregion = new Property(13, String.class, "entregion", false, "ENTREGION");
        public final static Property Oisuper = new Property(14, String.class, "oisuper", false, "OISUPER");
        public final static Property Oisupername = new Property(15, String.class, "oisupername", false, "OISUPERNAME");
        public final static Property Inspstatus = new Property(16, String.class, "inspstatus", false, "INSPSTATUS");
        public final static Property UserId = new Property(17, String.class, "userId", false, "USER_ID");
        public final static Property Belongedarea = new Property(18, String.class, "belongedarea", false, "BELONGEDAREA");
        public final static Property LegalName = new Property(19, String.class, "legalName", false, "LEGAL_NAME");
        public final static Property IsSearched = new Property(20, String.class, "isSearched", false, "IS_SEARCHED");
    }


    public RegulateObjectBeanDao(DaoConfig config) {
        super(config);
    }
    
    public RegulateObjectBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"REGULATE_OBJECT_BEAN\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"NATURE\" TEXT," + // 2: nature
                "\"CODE\" TEXT," + // 3: code
                "\"UNIFIED_CODE\" TEXT," + // 4: unifiedCode
                "\"BELONGED_TRADE\" TEXT," + // 5: belongedTrade
                "\"ADDRESS\" TEXT," + // 6: address
                "\"LONGITUDE\" REAL NOT NULL ," + // 7: longitude
                "\"LATITUDE\" REAL NOT NULL ," + // 8: latitude
                "\"CONTACT_PHONE\" TEXT," + // 9: contactPhone
                "\"INSPCOUNT\" TEXT," + // 10: inspcount
                "\"ENTCREDIT\" TEXT," + // 11: entcredit
                "\"PASSRATE\" TEXT," + // 12: passrate
                "\"ENTREGION\" TEXT," + // 13: entregion
                "\"OISUPER\" TEXT," + // 14: oisuper
                "\"OISUPERNAME\" TEXT," + // 15: oisupername
                "\"INSPSTATUS\" TEXT," + // 16: inspstatus
                "\"USER_ID\" TEXT," + // 17: userId
                "\"BELONGEDAREA\" TEXT," + // 18: belongedarea
                "\"LEGAL_NAME\" TEXT," + // 19: legalName
                "\"IS_SEARCHED\" TEXT);"); // 20: isSearched
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"REGULATE_OBJECT_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RegulateObjectBean entity) {
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
 
        String belongedTrade = entity.getBelongedTrade();
        if (belongedTrade != null) {
            stmt.bindString(6, belongedTrade);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(7, address);
        }
        stmt.bindDouble(8, entity.getLongitude());
        stmt.bindDouble(9, entity.getLatitude());
 
        String contactPhone = entity.getContactPhone();
        if (contactPhone != null) {
            stmt.bindString(10, contactPhone);
        }
 
        String inspcount = entity.getInspcount();
        if (inspcount != null) {
            stmt.bindString(11, inspcount);
        }
 
        String entcredit = entity.getEntcredit();
        if (entcredit != null) {
            stmt.bindString(12, entcredit);
        }
 
        String passrate = entity.getPassrate();
        if (passrate != null) {
            stmt.bindString(13, passrate);
        }
 
        String entregion = entity.getEntregion();
        if (entregion != null) {
            stmt.bindString(14, entregion);
        }
 
        String oisuper = entity.getOisuper();
        if (oisuper != null) {
            stmt.bindString(15, oisuper);
        }
 
        String oisupername = entity.getOisupername();
        if (oisupername != null) {
            stmt.bindString(16, oisupername);
        }
 
        String inspstatus = entity.getInspstatus();
        if (inspstatus != null) {
            stmt.bindString(17, inspstatus);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(18, userId);
        }
 
        String belongedarea = entity.getBelongedarea();
        if (belongedarea != null) {
            stmt.bindString(19, belongedarea);
        }
 
        String legalName = entity.getLegalName();
        if (legalName != null) {
            stmt.bindString(20, legalName);
        }
 
        String isSearched = entity.getIsSearched();
        if (isSearched != null) {
            stmt.bindString(21, isSearched);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RegulateObjectBean entity) {
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
 
        String belongedTrade = entity.getBelongedTrade();
        if (belongedTrade != null) {
            stmt.bindString(6, belongedTrade);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(7, address);
        }
        stmt.bindDouble(8, entity.getLongitude());
        stmt.bindDouble(9, entity.getLatitude());
 
        String contactPhone = entity.getContactPhone();
        if (contactPhone != null) {
            stmt.bindString(10, contactPhone);
        }
 
        String inspcount = entity.getInspcount();
        if (inspcount != null) {
            stmt.bindString(11, inspcount);
        }
 
        String entcredit = entity.getEntcredit();
        if (entcredit != null) {
            stmt.bindString(12, entcredit);
        }
 
        String passrate = entity.getPassrate();
        if (passrate != null) {
            stmt.bindString(13, passrate);
        }
 
        String entregion = entity.getEntregion();
        if (entregion != null) {
            stmt.bindString(14, entregion);
        }
 
        String oisuper = entity.getOisuper();
        if (oisuper != null) {
            stmt.bindString(15, oisuper);
        }
 
        String oisupername = entity.getOisupername();
        if (oisupername != null) {
            stmt.bindString(16, oisupername);
        }
 
        String inspstatus = entity.getInspstatus();
        if (inspstatus != null) {
            stmt.bindString(17, inspstatus);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(18, userId);
        }
 
        String belongedarea = entity.getBelongedarea();
        if (belongedarea != null) {
            stmt.bindString(19, belongedarea);
        }
 
        String legalName = entity.getLegalName();
        if (legalName != null) {
            stmt.bindString(20, legalName);
        }
 
        String isSearched = entity.getIsSearched();
        if (isSearched != null) {
            stmt.bindString(21, isSearched);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public RegulateObjectBean readEntity(Cursor cursor, int offset) {
        RegulateObjectBean entity = new RegulateObjectBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nature
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // code
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // unifiedCode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // belongedTrade
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // address
            cursor.getDouble(offset + 7), // longitude
            cursor.getDouble(offset + 8), // latitude
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // contactPhone
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // inspcount
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // entcredit
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // passrate
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // entregion
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // oisuper
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // oisupername
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // inspstatus
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // userId
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // belongedarea
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // legalName
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20) // isSearched
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RegulateObjectBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNature(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCode(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUnifiedCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBelongedTrade(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAddress(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLongitude(cursor.getDouble(offset + 7));
        entity.setLatitude(cursor.getDouble(offset + 8));
        entity.setContactPhone(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setInspcount(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setEntcredit(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setPassrate(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setEntregion(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setOisuper(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setOisupername(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setInspstatus(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setUserId(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setBelongedarea(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setLegalName(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setIsSearched(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
     }
    
    @Override
    protected final String updateKeyAfterInsert(RegulateObjectBean entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(RegulateObjectBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RegulateObjectBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
