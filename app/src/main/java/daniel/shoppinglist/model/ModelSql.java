package daniel.shoppinglist.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 04/07/2016.
 */
public class ModelSql {
    final static int VERSION = 6;

    Helper sqlDb;


    public ModelSql(Context context){
        sqlDb = new Helper(context);
    }

    public SQLiteDatabase getWritableDB(){
        return sqlDb.getWritableDatabase();
    }

    public SQLiteDatabase getReadbleDB(){
        return sqlDb.getReadableDatabase();
    }

    class Helper extends SQLiteOpenHelper {
        public Helper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //StudentSql.create(db);
            LastUpdateSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //StudentSql.drop(db);
            LastUpdateSql.drop(db);
            onCreate(db);
        }
    }
}
