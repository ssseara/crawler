package br.com.editoraglobo.crawler.resource;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.editoraglobo.crawler.util.WebCrawler;

@RestController
@RequestMapping("/feed")
public class FeedResource {
	
	@GetMapping("/obter")
	public ResponseEntity<?> obter(@RequestParam String url){
		try {
			JSONObject json = WebCrawler.crawl(url);
			return json != null ? ResponseEntity.ok(json.toString()) : ResponseEntity.noContent().build();
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
}
