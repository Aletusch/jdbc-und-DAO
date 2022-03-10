package dataaccess;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String courselist_error_ocurred) {
        super(courselist_error_ocurred);

    }
}
