package fabien.validators;

import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;

public class NoteValidator extends Validator {
    public NoteValidator(String customErrorMessage) {
        super(customErrorMessage);
    }

    @Override
    public boolean isValid(EditText editText) {
        return Double.valueOf(editText.getText().toString()) <= 20.0;
    }
}
