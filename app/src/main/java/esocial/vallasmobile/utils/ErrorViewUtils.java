package esocial.vallasmobile.utils;

import android.content.Context;
import android.view.View;

import tr.xip.errorview.ErrorView;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class ErrorViewUtils {

    public static void showError(ErrorView errorView, String title, String subtitle,
                                 String retryText, ErrorView.RetryListener listener) {

        ErrorView.Config config = errorView.getConfig().create()
                .title(title)
                .subtitle(subtitle)
                .retryText(retryText)
                .build();
        errorView.setConfig(config);
        errorView.setVisibility(View.VISIBLE);
        errorView.setOnRetryListener(listener);
    }

    public static void showErrorWithImage(ErrorView errorView, String title, String subtitle,
                                          String retryText, int imageRes, ErrorView.RetryListener listener) {
        ErrorView.Config config = errorView.getConfig().create()
                .title(title)
                .subtitle(subtitle)
                .retryText(retryText)
                .image(imageRes)
                .build();
        errorView.setConfig(config);
        errorView.setVisibility(View.VISIBLE);
        errorView.setOnRetryListener(listener);
    }
}
