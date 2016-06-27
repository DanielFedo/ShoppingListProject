package daniel.shoppinglist;

/**
 * Created by Daniel on 27/06/2016.
 */
public class Utils {

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidPassword(CharSequence target){
        if (target == null || target.length() < 3)
            return false;

        return true;
    }

}
