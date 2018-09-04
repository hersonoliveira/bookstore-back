package com.home.bookstoreback;

import com.home.bookstoreback.model.Book;
import com.home.bookstoreback.model.BookRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
				hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
		Assert.assertNotNull("The JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup(){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		bookRepository.deleteAll();

		this.bookList.add(bookRepository.save(new Book("Test book 1", "This is a test book")));
		this.bookList.add(bookRepository.save(new Book("Test book 2", "This is a test book")));
	}

	@Test
	public void bookNotFound() throws Exception {
		this.mockMvc.perform(get("/books/999"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void createBook() throws Exception {
		String bookJson = json(new Book("Test book 3", "This is a test book"));

		this.mockMvc.perform(post("/books")
				.contentType(contentType)
				.content(bookJson))
				.andExpect(status().isCreated());
	}

	@Test
	public void searchBook() throws Exception {
		mockMvc.perform(get("/books"))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].title", is(bookList.get(0).getTitle())))
				.andExpect(jsonPath("$[0].description", is(bookList.get(0).getDescription())))
				.andExpect(jsonPath("$[1].title", is(bookList.get(1).getTitle())))
				.andExpect(jsonPath("$[1].description", is(bookList.get(1).getDescription())));
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
