package by.pavel.mytutby.ui.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import by.pavel.mytutby.R;
import by.pavel.mytutby.databinding.FragmentItemBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ItemFragment extends Fragment {

    private FragmentItemBinding binding;
    private ItemViewModel viewModel;
    private ItemFragmentArgs args;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        if (getArguments() != null)
            args = ItemFragmentArgs.fromBundle(getArguments());
        viewModel.loadItem(args.getUrl());
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (binding != null)
                    binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (binding != null)
                    binding.progressBar.setVisibility(View.GONE);
                viewModel.markReading();
            }
        });
//        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(args.getUrl());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_web, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                if (binding.webView.canGoBack())
                    binding.webView.goBack();
                break;
            case R.id.action_forward:
                if (binding.webView.canGoForward())
                    binding.webView.goForward();
                break;
            case R.id.action_refresh:
                binding.webView.reload();
                break;
            case R.id.action_shop:
                binding.webView.stopLoading();
                break;
            case R.id.action_launch:
                launchBrowser();
                break;
            case R.id.mark_done:
                viewModel.markDone();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchBrowser() {
        Uri uri = Uri.parse(args.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
