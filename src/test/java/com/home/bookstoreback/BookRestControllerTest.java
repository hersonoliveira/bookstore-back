package com.home.bookstoreback;

import com.home.bookstoreback.model.Book;
import com.home.bookstoreback.model.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class BookRestControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private MockMvc mockMvc;

	private List<Book> bookList = new ArrayList<>();

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private BookRepository bookRepository;

	@Before
	public void setup(){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		this.bookList.add(bookRepository.save(new Book("Test book 1", "This is a test book")));
		this.bookList.add(bookRepository.save(new Book("Test book 2", "This is a test book")));
	}

//	@Test
//	public void bookNotFound(){}

//	@Test
//	public void createBook(){}

	@Test
	public void searchBook() throws Exception {
		mockMvc.perform(get("/books"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].title", is(bookList.get(0).getTitle())))
				.andExpect(jsonPath("$[0].description", is(bookList.get(0).getDescription())))
				.andExpect(jsonPath("$[1].title", is(bookList.get(1).getTitle())))
				.andExpect(jsonPath("$[1].description", is(bookList.get(1).getDescription())));
	}

}
