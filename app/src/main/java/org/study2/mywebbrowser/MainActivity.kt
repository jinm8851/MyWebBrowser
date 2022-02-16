package org.study2.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import org.study2.mywebbrowser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        웹뷰 기본설정
        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.urlEditText.setText(url)
                }
            }
        }

        binding.webView.loadUrl("https://www.google.com")

        /*setOnEditorActionListener 는 에디트텍스트가 선택되고 글자가 입력될때 호출.
        * 인자로 반응한뷰,액션ID, 이벤트 세가지면, 여기서는 뷰와 이벤트를 사용하지않기위해 _로 대치할수있음
        * actionId 값은 EditorInfo 클래스에 상수로 정의된 ㄱㅄ 중에서 검색 버튼에 해당하는 상수와 비교하여 검색 버튼이 눌렸는지 확인합니다
        * 검색창에 입력한 주소를 웹뷰에 전달하여 로딩합니다
        * true를 반환하여 이벤트를 종료합니다.*/

        binding.urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.webView.loadUrl(binding.urlEditText.text.toString())
                true
            } else {
                false
            }
        }

//        컨텍스트 메뉴 등록
        registerForContextMenu(binding.webView)
    }
/*웹뷰가 이전 페이지로 갈수있다면 이전페이지로 이동하고 그렇지 않다면 원래 동작을 수행합니다.즉 엑티비티를 종료합니다.`*/
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
    /*메뉴 리소스 파일을 지정하면 메뉴가 표시됩니다
    메뉴 리소스를 액티비티의 메뉴로 표시하려면 menuInflater 객체의 inflate() 메서드를 사용하여 메뉴 리소스를 지정합니다.
    true 를 반환하면 액티비티에 메뉴가 있다고 인식합니다.*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_google, R.id.action_home -> {
                binding.webView.loadUrl("https://www.google.com")
                return true
            }
            R.id.action_naver -> {
                binding.webView.loadUrl("https://www.naver.com")
                return true
            }
            R.id.action_daum -> {
                binding.webView.loadUrl("https://ww.daum.net")
                return true
            }
            R.id.action_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:010-4114-8228")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                binding.webView.url?.let { url ->
                    //문자 보내기 Extension.kt
                    sendSMS("tel:010-2031-8228",url)
                }
                return true
            }
            R.id.action_email -> {
                binding.webView.url?.let { url ->
                    //이메일 보내기 Extension.kt
                    email("jinm8851@gmail.com","good site",url)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

   /* menuInflater.inflate(R.menu.context,menu)메서드를 사용하여 메뉴 리소스를 액티비티의 컨텍스트 메뉴로서 사용하도록합니다
    ( 컨텍스트 메뉴 등록  registerForContextMenu(binding.webView))*/
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_shere -> {
                binding.webView.url?.let { url ->
                    //페이지 공유 Extension.kt
                    share(url)
                }
                return true
            }
            R.id.action_browser -> {
                binding.webView.url?.let { url ->
                    // 기본 웹 브라우저에서 열기 Extension.kt
                    browse(url)
                }
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}