package it.rainbowbreeze.playtog.data.provider.base;

import android.database.Cursor;
import android.database.CursorWrapper;

import it.rainbowbreeze.playtog.data.provider.player.PlayerColumns;

public class AliasCursor extends CursorWrapper {
    public AliasCursor(Cursor cursor) {
        super(cursor);
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        int res = getColumnIndex(columnName);
        if (res == -1) throw new IllegalArgumentException("column '" + columnName + "' does not exist");
        return res;
    }

    @Override
    public int getColumnIndex(String columnName) {
        int res = super.getColumnIndex(columnName);
        if (res == -1) {
            // Could not find the column, try with its alias
            String alias = getAlias(columnName);
            if (alias == null) return -1;
            return super.getColumnIndex(alias);
        }
        return res;
    }

    private static String getAlias(String columnName) {
        String res = null;
        // Player
        res = PlayerColumns.getAlias(columnName);
        if (res != null) return res;

        return null;
    }
}