package findboom.android.com.findboom.database;/**
 * Created by Administrator on 2016/9/20.
 */

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.wxpay.Constants;

/**
 * author:${白曌勇} on 2016/9/20
 * TODO:
 */
public class BoomSqliteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static BoomSqliteHelper instance;

    private String CREATE_USER_TABLE = "CREATE TABLE " + Contents.USER_TABLE + " ("
            + Contents.USER_BALANCE + " TEXT, "
            + Contents.USER_EASEID + " TEXT, " + Contents.USER_EASEPWD + " TEXT, "
            + Contents.USER_GIVETIME + " TEXT, " + Contents.USER_ID + " TEXT PRIMARY KEY, "
            + Contents.USER_ISADMIN + " INTEGER, " + Contents.USER_LEVEL + " TEXT, "
            + Contents.USER_PHONE + " TEXT, " + Contents.USER_CREATETIME + " TEXT, "
            + Contents.USER_REDPACK + " TEXT, " + Contents.USER_REFERCOUNT + " TEXT, "
            + Contents.USER_SCORE + " TEXT, " + Contents.USER_STATUS + " TEXT, "
            + Contents.USER_AVATAR + " TEXT, " + Contents.USER_NIKC + " TEXT, "
            + Contents.USER_AGE + " TEXT, "
            + Contents.USER_SOURCE + " TEXT, " + Contents.USER_STATUSTXT + " TEXT);";

    private String CREATE_COMMON_TABLE = "CREATE TABLE " + Contents.COMMON_TABLE + " ("
            + Contents.COMMON_DAYS + " TEXT, " + Contents.COMMON_INTRO + " TEXT, "
            + Contents.COMMON_PLAUS + " INTEGER, " + Contents.COMMON_RANGE + " INTEGER, "
            + Contents.COMMON_ID + " INTEGER, "
            + Contents.COMMON_REDUCE + " INTEGER, " + Contents.COMMON_TYPE + " TEXT);";

    private String CREATE_RED_TABLE = "CREATE TABLE " + Contents.RED_TABLE + " ("
            + Contents.COMMON_DAYS + " TEXT, " + Contents.RED_ID + " INTEGER, "
            + Contents.RED_MAX_AMOUNT + " INTEGER, " + Contents.RED_MAX_COUNT + " INTEGER, "
            + Contents.RED_MIN_AMOUNT + " INTEGER, " + Contents.COMMON_TYPE + " TEXT);";

    private String CREATE_STRING_TABLE = "CREATE TABLE " + Contents.STRING_TABLE + " ("
            + Contents.STRING_CONTENT + " TEXT, "
            + Contents.STRING_TYPE + " INTEGER);";

    public static BoomSqliteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new BoomSqliteHelper(context.getApplicationContext());
        }
        return instance;
    }

    private BoomSqliteHelper(Context context) {
        super(context, getUserDatabaseName(), null, DATABASE_VERSION);
    }

    private static String getUserDatabaseName() {
        return "user_boom.db";
    }

    public BoomSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BoomSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_COMMON_TABLE);
        db.execSQL(CREATE_RED_TABLE);
        db.execSQL(CREATE_STRING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
