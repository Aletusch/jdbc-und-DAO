package dataaccess;

public class DatabaseExcepton extends RuntimeException {
    public DatabaseExcepton(String courselist_error_ocurred) {
        super(courselist_error_ocurred);

    }
}
