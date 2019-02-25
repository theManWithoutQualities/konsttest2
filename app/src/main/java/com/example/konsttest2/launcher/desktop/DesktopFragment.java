package com.example.konsttest2.launcher.desktop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konsttest2.R;
import com.example.konsttest2.data.LauncherDbHelper;
import com.example.konsttest2.data.Link;
import com.example.konsttest2.metrica.MetricaUtils;
import com.yandex.metrica.YandexMetrica;

import java.util.List;

public class DesktopFragment extends Fragment {

    private LauncherDbHelper launcherDbHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        launcherDbHelper = new LauncherDbHelper(context);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        final View view = inflater.inflate(
                R.layout.fragment_desktop_page,
                container,
                false
        );

        for(int ind1 = 0; ind1 < ((ViewGroup)view).getChildCount(); ind1 ++) {
            final View row = ((ViewGroup) view).getChildAt(ind1);
            for(int ind2 = 0; ind2 < ((ViewGroup)row).getChildCount(); ind2 ++) {
                final View cell = ((ViewGroup) row).getChildAt(ind2);
                cell.setTag("cell_" + ind1 + "_" + ind2);
                cell.setOnLongClickListener((v) -> {
                    YandexMetrica.reportEvent(MetricaUtils.LONGCLICK_DESKTOP);
                    v.startActionMode(actionModeCallBack.setClickedView(v));
                    return true;
                });
            }
        }
        final List<Link> links = launcherDbHelper.getAllLinks();
        for(Link link : links) {
            final View viewLink = view.findViewWithTag("cell_" + link.getX() + "_" + link.getY());
            final ImageView image = (ImageView) ((ViewGroup) viewLink).getChildAt(0);
            final TextView text = (TextView) ((ViewGroup) viewLink).getChildAt(1);
            text.setText(link.getTitle());
            new DownloadImageTask(image).execute(link.getWeblink());
        }
        return view;
    }

    private interface ActionModeCallback extends ActionMode.Callback {
        ActionModeCallback setClickedView(View view);
    }

    private ActionModeCallback actionModeCallBack = new ActionModeCallback() {

        private View clickedView;

        @Override
        public ActionModeCallback setClickedView(View view) {
            this.clickedView = view;
            return this;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.desktop_context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.context_delete:
                    YandexMetrica.reportEvent(MetricaUtils.CONTEXT_DELETE_LINK);
                    ((ViewGroup)clickedView).removeAllViews();
                    mode.finish();
                    return true;
                case R.id.context_add:
                    YandexMetrica.reportEvent(MetricaUtils.CONTEXT_ADD_LINK);
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.desktop_dialog);
                    dialog.setCancelable(true);
                    dialog.show();
                    final Button button = dialog.findViewById(R.id.button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String title = ((EditText) dialog.findViewById(R.id.address))
                                    .getText()
                                    .toString();
                            final String weblink = ((EditText) dialog.findViewById(R.id.address))
                                    .getText()
                                    .toString();
                            int x =Integer
                                    .valueOf(((String)clickedView.getTag()).split("_")[1]);
                            int y =Integer
                                    .valueOf(((String)clickedView.getTag()).split("_")[2]);
                            final Link link = new Link()
                                    .setTitle(title)
                                    .setWeblink(weblink)
                                    .setX(x)
                                    .setY(y);
                            launcherDbHelper.saveLinkAndDeleteOld(link);
                            dialog.dismiss();
                        }
                    });
                    mode.finish();
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };
}
