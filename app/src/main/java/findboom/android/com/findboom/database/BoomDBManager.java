package findboom.android.com.findboom.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.bean.Bean_AllConfig;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.wxpay.Constants;

public class BoomDBManager {
    static private BoomDBManager dbMgr = new BoomDBManager();
    private BoomSqliteHelper dbHelper;

    private BoomDBManager() {
        dbHelper = BoomSqliteHelper.getInstance(FindBoomApplication.getInstance());
    }

    public static synchronized BoomDBManager getInstance() {
        if (dbMgr == null) {
            dbMgr = new BoomDBManager();
        }
        return dbMgr;
    }

    public void setUserData(Bean_UserInfo.GameUser user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
//            try {
//                db.delete(Contents.USER_TABLE, Contents.USER_ID + "= " + user.GameUserId, null);
//            } catch (Exception e) {
//            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(Contents.USER_BALANCE, user.UserBalance);
            contentValues.put(Contents.USER_CREATETIME, user.CreateTime);
            contentValues.put(Contents.USER_EASEID, user.EasemobId);
            contentValues.put(Contents.USER_EASEPWD, user.EasemobPwd);
            contentValues.put(Contents.USER_GIVETIME, user.LastGiveTime);
            contentValues.put(Contents.USER_ID, user.GameUserId);
            contentValues.put(Contents.USER_ISADMIN, user.IsAdminUser ? 1 : 0);
            contentValues.put(Contents.USER_LEVEL, user.UserLevel);
            contentValues.put(Contents.USER_PHONE, user.PhoneNumber);
            contentValues.put(Contents.USER_REDPACK, user.RedPackBalance);
            contentValues.put(Contents.USER_REFERCOUNT, user.ReferUserCount);
            contentValues.put(Contents.USER_SCORE, user.UserScore);
            contentValues.put(Contents.USER_SOURCE, user.UserSource);
            contentValues.put(Contents.USER_STATUS, user.Status);
            contentValues.put(Contents.USER_STATUSTXT, user.StatusTxt);
            contentValues.put(Contents.USER_AGE, user.Age);
            contentValues.put(Contents.USER_NIKC, user.NickName);
            contentValues.put(Contents.USER_AVATAR, user.Avatar);
            db.replace(Contents.USER_TABLE, null, contentValues);
        }
    }

    public void updateUserData(Bean_UserInfo.GameUser user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(Contents.USER_TABLE, null, null);
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contents.USER_BALANCE, user.UserBalance);
            contentValues.put(Contents.USER_CREATETIME, user.CreateTime);
            contentValues.put(Contents.USER_EASEID, user.EasemobId);
            contentValues.put(Contents.USER_EASEPWD, user.EasemobPwd);
            contentValues.put(Contents.USER_GIVETIME, user.LastGiveTime);
            contentValues.put(Contents.USER_ID, user.GameUserId);
            contentValues.put(Contents.USER_ISADMIN, user.IsAdminUser ? 1 : 0);
            contentValues.put(Contents.USER_LEVEL, user.UserLevel);
            contentValues.put(Contents.USER_PHONE, user.PhoneNumber);
            contentValues.put(Contents.USER_REDPACK, user.RedPackBalance);
            contentValues.put(Contents.USER_REFERCOUNT, user.ReferUserCount);
            contentValues.put(Contents.USER_SCORE, user.UserScore);
            contentValues.put(Contents.USER_SOURCE, user.UserSource);
            contentValues.put(Contents.USER_STATUS, user.Status);
            contentValues.put(Contents.USER_STATUSTXT, user.StatusTxt);
            contentValues.put(Contents.USER_AGE, user.Age);
            contentValues.put(Contents.USER_NIKC, user.NickName);
            contentValues.put(Contents.USER_AVATAR, user.Avatar);
            db.replace(Contents.USER_TABLE, null, contentValues);
        }
    }

    public Bean_UserInfo.GameUser getUserData(String userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Bean_UserInfo.GameUser user = new Bean_UserInfo.GameUser();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + Contents.USER_TABLE + " where " + Contents.USER_ID + " = " + userId, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    user.GameUserId = userId;
                    user.UserBalance = cursor.getString(cursor.getColumnIndex(Contents.USER_BALANCE));
                    user.CreateTime = cursor.getString(cursor.getColumnIndex(Contents.USER_CREATETIME));
                    user.EasemobId = cursor.getString(cursor.getColumnIndex(Contents.USER_EASEID));
                    user.EasemobPwd = cursor.getString(cursor.getColumnIndex(Contents.USER_EASEPWD));
                    user.LastGiveTime = cursor.getString(cursor.getColumnIndex(Contents.USER_GIVETIME));
                    user.IsAdminUser = cursor.getInt(cursor.getColumnIndex(Contents.USER_ISADMIN)) > 0;
                    user.UserLevel = cursor.getString(cursor.getColumnIndex(Contents.USER_LEVEL));
                    user.PhoneNumber = cursor.getString(cursor.getColumnIndex(Contents.USER_PHONE));
                    user.RedPackBalance = cursor.getString(cursor.getColumnIndex(Contents.USER_REDPACK));
                    user.ReferUserCount = cursor.getString(cursor.getColumnIndex(Contents.USER_REFERCOUNT));
                    user.UserScore = cursor.getString(cursor.getColumnIndex(Contents.USER_SCORE));
                    user.Status = cursor.getString(cursor.getColumnIndex(Contents.USER_STATUS));
                    user.StatusTxt = cursor.getString(cursor.getColumnIndex(Contents.USER_STATUSTXT));
                    user.Age = cursor.getString(cursor.getColumnIndex(Contents.USER_AGE));
                    user.NickName = cursor.getString(cursor.getColumnIndex(Contents.USER_NIKC));
                    user.Avatar = cursor.getString(cursor.getColumnIndex(Contents.USER_AVATAR));
                }
                cursor.close();
            }
        }
        return user;
    }

    /**
     * 普通雷数据
     *
     * @param id
     * @param commonBoom
     */
    synchronized public void setCommonData(int id, Bean_AllConfig.CommonBoom commonBoom) {
        if (commonBoom == null)
            return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(Contents.COMMON_TABLE, Contents.COMMON_ID + " = " + "\"" + id + "\"", null);
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contents.COMMON_ID, id);
            contentValues.put(Contents.COMMON_DAYS, commonBoom.ValidDays);
            contentValues.put(Contents.COMMON_INTRO, commonBoom.ArmDesc);
            contentValues.put(Contents.COMMON_PLAUS, commonBoom.PlusScore);
            contentValues.put(Contents.COMMON_RANGE, commonBoom.BombRange);
            contentValues.put(Contents.COMMON_REDUCE, commonBoom.MinusScore);
            contentValues.put(Contents.COMMON_TYPE, commonBoom.MineType);
            db.replace(Contents.COMMON_TABLE, null, contentValues);
        }
    }

    synchronized public Bean_AllConfig.CommonBoom getCommonData(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Bean_AllConfig.CommonBoom commonBoom = new Bean_AllConfig.CommonBoom();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + Contents.COMMON_TABLE + " where " + Contents.COMMON_ID + " = " + "\"" + id + "\"", null);
            while (cursor.moveToNext()) {
                commonBoom.ArmDesc = cursor.getString(cursor.getColumnIndex(Contents.COMMON_INTRO));
                commonBoom.BombRange = cursor.getInt(cursor.getColumnIndex(Contents.COMMON_RANGE));
                commonBoom.MineType = cursor.getString(cursor.getColumnIndex(Contents.COMMON_TYPE));
                commonBoom.MinusScore = cursor.getInt(cursor.getColumnIndex(Contents.COMMON_REDUCE));
                commonBoom.PlusScore = cursor.getInt(cursor.getColumnIndex(Contents.COMMON_PLAUS));
                commonBoom.ValidDays = cursor.getString(cursor.getColumnIndex(Contents.COMMON_DAYS));
            }
            cursor.close();
        }

        return commonBoom;
    }

    /**
     * 图片雷数据
     *
     * @param id
     * @param commonBoom
     */
    synchronized public void setPicData(int id, Bean_AllConfig.PicBoom commonBoom) {
        if (commonBoom == null)
            return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(Contents.COMMON_TABLE, Contents.COMMON_ID + " = " + "\"" + id + "\"", null);
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contents.COMMON_ID, id);
            contentValues.put(Contents.COMMON_DAYS, commonBoom.ValidDays);
            contentValues.put(Contents.COMMON_INTRO, commonBoom.ArmDesc);
            contentValues.put(Contents.COMMON_PLAUS, commonBoom.PlusScore);
            contentValues.put(Contents.COMMON_RANGE, commonBoom.BombRange);
            contentValues.put(Contents.COMMON_REDUCE, commonBoom.MinusScore);
            contentValues.put(Contents.COMMON_TYPE, commonBoom.MineType);
            db.replace(Contents.COMMON_TABLE, null, contentValues);
        }
    }

    synchronized public Bean_AllConfig.PicBoom getPicData(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Bean_AllConfig.PicBoom commonBoom = new Bean_AllConfig.PicBoom();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + Contents.COMMON_TABLE + " where " + Contents.COMMON_ID + " = " + "\"" + id + "\"", null);
            while (cursor.moveToNext()) {
                commonBoom.ArmDesc = cursor.getString(cursor.getColumnIndex(Contents.COMMON_INTRO));
                commonBoom.BombRange = cursor.getInt(cursor.getColumnIndex(Contents.COMMON_RANGE));
                commonBoom.MineType = cursor.getString(cursor.getColumnIndex(Contents.COMMON_TYPE));
                commonBoom.MinusScore = cursor.getInt(cursor.getColumnIndex(Contents.COMMON_REDUCE));
                commonBoom.PlusScore = cursor.getInt(cursor.getColumnIndex(Contents.COMMON_PLAUS));
                commonBoom.ValidDays = cursor.getString(cursor.getColumnIndex(Contents.COMMON_DAYS));
            }
            cursor.close();
        }

        return commonBoom;
    }

    /**
     * 文字雷数据
     *
     * @param id
     * @param commonBoom
     */
    synchronized public void setTextData(int id, Bean_AllConfig.TextBoom commonBoom) {
        if (commonBoom == null)
            return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(Contents.COMMON_TABLE, Contents.COMMON_ID + " = " + "\"" + id + "\"", null);
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contents.COMMON_ID, id);
            contentValues.put(Contents.COMMON_DAYS, commonBoom.ValidDays);
            contentValues.put(Contents.COMMON_INTRO, commonBoom.ArmDesc);
            contentValues.put(Contents.COMMON_PLAUS, commonBoom.PlusScore);
            contentValues.put(Contents.COMMON_RANGE, commonBoom.BombRange);
            contentValues.put(Contents.COMMON_REDUCE, commonBoom.MinusScore);
            contentValues.put(Contents.COMMON_TYPE, commonBoom.MineType);
            db.replace(Contents.COMMON_TABLE, null, contentValues);
            setStringList(commonBoom.Texts, id);
        }
    }

    synchronized public void setStringList(String[] data, int id) {
        if (data == null || data.length == 0)
            return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(Contents.STRING_TABLE, Contents.STRING_TYPE + " = " + "\"" + id + "\"", null);
            for (int i = 0; i < data.length; i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Contents.STRING_CONTENT, data[i]);
                contentValues.put(Contents.STRING_TYPE, id);
                db.replace(Contents.STRING_TABLE, null, contentValues);
            }

        }
    }

    synchronized public String[] getStringList(int id) {
        List<String> data = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + Contents.STRING_TABLE + " where " + Contents.STRING_TYPE + " = " + "\"" + id + "\"", null);
            while (cursor.moveToNext()) {
                data.add(cursor.getString(cursor.getColumnIndex(Contents.STRING_CONTENT)));
            }
            cursor.close();
        }
        return (String[]) data.toArray();
    }

    /**
     * 红包雷数据
     *
     * @param id
     * @param commonBoom
     */
    synchronized public void setRedData(int id, Bean_AllConfig.RedBoom commonBoom) {
        if (commonBoom == null)
            return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(Contents.COMMON_TABLE, Contents.COMMON_ID + " = " + "\"" + id + "\"", null);
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contents.RED_ID, id);
            contentValues.put(Contents.COMMON_DAYS, commonBoom.ValidDays);
            contentValues.put(Contents.COMMON_INTRO, commonBoom.ArmDesc);
            contentValues.put(Contents.COMMON_RANGE, commonBoom.BombRange);
            contentValues.put(Contents.COMMON_TYPE, commonBoom.MineType);
            contentValues.put(Contents.RED_MAX_AMOUNT, commonBoom.MaxAmount);
            contentValues.put(Contents.RED_MAX_COUNT, commonBoom.MaxCount);
            contentValues.put(Contents.RED_MIN_AMOUNT, commonBoom.MinAmount);
            db.replace(Contents.COMMON_TABLE, null, contentValues);
        }
    }

    synchronized public Bean_AllConfig.RedBoom getRedData(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Bean_AllConfig.RedBoom commonBoom = new Bean_AllConfig.RedBoom();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + Contents.RED_TABLE + " where " + Contents.RED_ID + " = " + "\"" + id + "\"", null);
            while (cursor.moveToNext()) {
                commonBoom.ArmDesc = cursor.getString(cursor.getColumnIndex(Contents.COMMON_INTRO));
                commonBoom.BombRange = cursor.getInt(cursor.getColumnIndex(Contents.COMMON_RANGE));
                commonBoom.MineType = cursor.getString(cursor.getColumnIndex(Contents.COMMON_TYPE));
                commonBoom.ValidDays = cursor.getString(cursor.getColumnIndex(Contents.COMMON_DAYS));
                commonBoom.MaxAmount = cursor.getInt(cursor.getColumnIndex(Contents.RED_MAX_AMOUNT));
                commonBoom.MaxCount = cursor.getInt(cursor.getColumnIndex(Contents.RED_MAX_COUNT));
                commonBoom.MinAmount = cursor.getInt(cursor.getColumnIndex(Contents.RED_MIN_AMOUNT));
            }
            cursor.close();
        }

        return commonBoom;
    }

    /**
     * 寻宝雷数据
     *
     * @param id
     * @param commonBoom
     */
    synchronized public void setGoldData(int id, Bean_AllConfig.GoldBoom commonBoom) {
        if (commonBoom == null)
            return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(Contents.COMMON_TABLE, Contents.COMMON_ID + " = " + "\"" + id + "\"", null);
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contents.RED_ID, id);
            contentValues.put(Contents.COMMON_DAYS, commonBoom.ValidDays);
            contentValues.put(Contents.COMMON_RANGE, commonBoom.BombRange);
            contentValues.put(Contents.COMMON_TYPE, commonBoom.MineType);
            contentValues.put(Contents.RED_MAX_COUNT, commonBoom.MaxCount);
            db.replace(Contents.COMMON_TABLE, null, contentValues);
        }
    }

    synchronized public Bean_AllConfig.GoldBoom getGoldData(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Bean_AllConfig.GoldBoom commonBoom = new Bean_AllConfig.GoldBoom();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + Contents.RED_TABLE + " where " + Contents.RED_ID + " = " + "\"" + id + "\"", null);
            while (cursor.moveToNext()) {
                commonBoom.BombRange = cursor.getInt(cursor.getColumnIndex(Contents.COMMON_RANGE));
                commonBoom.MineType = cursor.getString(cursor.getColumnIndex(Contents.COMMON_TYPE));
                commonBoom.ValidDays = cursor.getString(cursor.getColumnIndex(Contents.COMMON_DAYS));
                commonBoom.MaxCount = cursor.getInt(cursor.getColumnIndex(Contents.RED_MAX_COUNT));
            }
            cursor.close();
        }

        return commonBoom;
    }

}
