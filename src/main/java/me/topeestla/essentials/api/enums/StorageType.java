package me.topeestla.essentials.api.enums;

public enum StorageType {


    MARIADB(true),
    H2(true),
    POSTGRESQL(true),
    MONGODB(false),
    JSON(false);



    private boolean sql;

    StorageType(boolean sql) {
        this.sql = sql;
    }

    public boolean isSql() {
        return sql;
    }
}
