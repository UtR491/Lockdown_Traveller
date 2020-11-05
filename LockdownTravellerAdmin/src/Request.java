import java.io.Serializable;

/**
 * Request abstract class. Just to make things scalable in case many request objects share some function or variables in
 * the future. Maybe User ID can be kept here.
 */
public abstract class Request implements Serializable {
}
