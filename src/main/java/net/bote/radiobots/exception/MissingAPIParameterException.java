package net.bote.radiobots.exception;

/**
 * @author Elias Arndt | bote100
 * Created on 17.07.2019
 */

public class MissingAPIParameterException extends RuntimeException {

    public MissingAPIParameterException() {
        super();
    }

    /**
     * Constructs a <code>MissingAPIParameterException</code> with the specified
     * detail message.
     *
     * @param   s   the detail message.
     */
    public MissingAPIParameterException(String s) {
        super(s);
    }

    /**
     * Constructs a <code>MissingAPIParameterException</code> with the specified
     * detail message and cause.
     * @param message the detail message
     * @param cause the detail cause
     */
    public MissingAPIParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a <code>MissingAPIParameterException</code> with the specified cause
     * and a detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A <tt>null</tt> value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     * @since 1.5
     */
    public MissingAPIParameterException(Throwable cause) {
        super(cause);
    }
    
}
