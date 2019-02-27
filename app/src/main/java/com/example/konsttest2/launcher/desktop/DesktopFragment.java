package com.example.konsttest2.launcher.desktop;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.example.konsttest2.data.DesktopItem;
import com.example.konsttest2.data.DesktopItemType;
import com.example.konsttest2.data.LauncherDbHelper;
import com.example.konsttest2.metrica.MetricaUtils;
import com.yandex.metrica.YandexMetrica;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class DesktopFragment extends Fragment {

    private LauncherDbHelper launcherDbHelper;
    private int currentItemX;
    private int currentItemY;
    static final int REQUEST_SELECT_CONTACT = 1;

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
        setItemsOnDesktop(view);
        return view;
    }

    private void setItemsOnDesktop(View view) {
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
        final List<DesktopItem> desktopItems = launcherDbHelper.getAllDesktopItems();
        for(DesktopItem desktopItem : desktopItems) {
            final View viewLink = view
                    .findViewWithTag("cell_" + desktopItem.getX() + "_" + desktopItem.getY());
            final ImageView image = (ImageView) ((ViewGroup) viewLink).getChildAt(0);
            final TextView title = (TextView) ((ViewGroup) viewLink).getChildAt(1);
            title.setText(desktopItem.getTitle());
            viewLink.setOnClickListener(v -> {
                final Intent intent =
                        new Intent(
                                "android.intent.action.VIEW",
                                Uri.parse(desktopItem.getLink())
                        );
                startActivity(intent);
            });
            new DownloadImageTask(image, getContext()).execute(desktopItem.getLink());
        }
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
            final int x = Integer
                    .valueOf(((String)clickedView.getTag()).split("_")[1]);
            final int y = Integer
                    .valueOf(((String)clickedView.getTag()).split("_")[2]);
            switch (item.getItemId()) {
                case R.id.context_delete:
                    YandexMetrica.reportEvent(MetricaUtils.CONTEXT_DELETE_LINK);
                    ((ImageView)((ViewGroup)clickedView)
                            .getChildAt(0))
                            .setImageResource(android.R.color.transparent);
                    ((TextView)((ViewGroup)clickedView)
                            .getChildAt(1))
                            .setText("");
                    final DesktopItem desktopItem = launcherDbHelper.getDesktopItemByXAndY(x, y);
                    if (desktopItem != null) {
                        launcherDbHelper.deleteDesktopItem(desktopItem.getId());
                    }
                    mode.finish();
                    return true;
                case R.id.context_add_link:
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
                            String weblink = ((EditText) dialog.findViewById(R.id.address))
                                    .getText()
                                    .toString();
                            if (!weblink.contains("http")) {
                                weblink = "http://" + weblink;
                            }
                            final DesktopItem desktopItem = new DesktopItem()
                                    .setTitle(title)
                                    .setLink(weblink)
                                    .setX(x)
                                    .setY(y)
                                    .setType(DesktopItemType.WEBLINK.ordinal());
                            launcherDbHelper.saveDesktopItemAndDeleteOld(desktopItem);
                            dialog.dismiss();
                            setItemsOnDesktop(getView());
                        }
                    });
                    mode.finish();
                    return true;
                case R.id.context_add_phone:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    currentItemX = x;
                    currentItemY = y;
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_SELECT_CONTACT);
                    }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            final DesktopItem desktopItem = new DesktopItem()
                    .setTitle("contact")
                    .setLink(contactUri.toString())
                    .setX(currentItemX)
                    .setY(currentItemY)
                    .setType(DesktopItemType.CONTACT.ordinal());
            launcherDbHelper.saveDesktopItemAndDeleteOld(desktopItem);
            setItemsOnDesktop(getView());
        }
    }
}
