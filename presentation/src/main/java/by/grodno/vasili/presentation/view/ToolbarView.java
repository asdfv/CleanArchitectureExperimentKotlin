package by.grodno.vasili.presentation.view;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import by.grodno.vasili.presentation.R;
import by.grodno.vasili.presentation.databinding.ToolbarViewBinding;

/**
 * Top toolbar with customizable title, ok and cancel handlers. <br>
 * Optional attributes for component in XML:
 * <ul>
 * <li>app:title - title for toolbar (default: empty) <br></li>
 * <li>app:onOkClick - handler for click on check-icon (default: close activity) <br></li>
 * <li>app:onCancelClick - handler for click on close-icon (default: close activity) <br></li>
 * </ul>
 */
public class ToolbarView extends ConstraintLayout {
    private ToolbarViewBinding binding;

    public ToolbarView(Context context) {
        super(context);
        init(context);
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(@NonNull Context context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.toolbar_view, this, true);
        setDefaultListeners((Activity) context, binding);
    }

    private void setDefaultListeners(Activity context, ToolbarViewBinding binding) {
        binding.checkImage.setOnClickListener(v -> context.finish());
        binding.closeImage.setOnClickListener(v -> context.finish());
    }

    /**
     * Setter for optional attribute app:title - title for toolbar
     */
    @SuppressWarnings("unused")
    public void setTitle(String title) {
        binding.toolbarTitle.setText(title);
    }

    /**
     * Setter for optional attribute app:onOkClick
     *
     * @param okHandler handler for check-icon pressing
     */
    @SuppressWarnings("unused")
    public void setOnOkClick(Runnable okHandler) {
        binding.checkImage.setOnClickListener(view -> okHandler.run());
    }

    /**
     * Setter for optional attribute app:onCancelClick
     *
     * @param cancelHandler handler for close-icon pressing
     */
    @SuppressWarnings("unused")
    public void setOnCancelClick(Runnable cancelHandler) {
        binding.closeImage.setOnClickListener(view -> cancelHandler.run());
    }
}
