package com.example.bookshop;

import com.example.bookshop.dao.*;
import com.example.bookshop.entity.*;
import com.example.bookshop.util.IsbnGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@RequiredArgsConstructor
public class BookShopApplication {

    private final AuthorDao authorDao;
    private final BookDao bookDao;
    private final GenreDao genreDao;
    private final PublisherDao publisherDao;
    private final RoleDao roleDao;
    @Bean
    @Transactional
    @Profile("security")
    public ApplicationRunner runner1(){
        return r -> {
            Role admin = new Role();
            admin.setRoleName("ROLE_ADMIN");

            Role user = new Role();
            user.setRoleName("ROLE_USER");

            roleDao.save(admin);
            roleDao.save(user);
        };
    }

    @Transactional
    @Profile("data") // for don't add data
    @Bean
    public ApplicationRunner runner() {
        return r -> {
            Author author1 = new Author("Charles Dickens", "charles@gmail.com");
            Author author2 = new Author("Thomas Hardy", "thomas@gmail.com");

            Publisher publisher1 = new Publisher("New Era", "newera@gmail.com");
            Publisher publisher2 = new Publisher("Sun", "sun@gmail.com");

            Genre genre1 = new Genre();
            genre1.setGenreName("Tragedy");
            Genre genre2 = new Genre();
            genre2.setGenreName("Adventure");

            Book book1 = new Book(
                    1,
                    IsbnGenerator.generic(),
                    "Oliver Twist",
                    "Excellent",
                    50.3,
                    20,
                    "http://source.unsplash.com/400x300/?hiking"
            );

            Book book2 = new Book(
                    2,
                    IsbnGenerator.generic(),
                    "Great Expectations",
                    "Good Choice",
                    25.3,
                    20,
                    "http://source.unsplash.com/400x300/?nature"
            );

            Book book3 = new Book(
                    3,
                    IsbnGenerator.generic(),
                    "Bleak House",
                    "Nice",
                    22.3,
                    20,
                    "http://source.unsplash.com/400x300/?ocean"
            );

            Book book4 = new Book(
                    4,
                    IsbnGenerator.generic(),
                    "Under the Greenwood Tree",
                    "Excellent",
                    50.3,
                    20,
                    "http://source.unsplash.com/400x300/?flower"
            );


            Book book5 = new Book(
                    5,
                    IsbnGenerator.generic(),
                    "Return of the Native",
                    "Excellent",
                    25.3,
                    20,
                    "http://source.unsplash.com/400x300/?home"
            );


            Book book6 = new Book(
                    6,
                    IsbnGenerator.generic(),
                    "Far From the Maddening Crowd",
                    "Excellent",
                    50.3,
                    20,
                    "http://source.unsplash.com/400x300/?alone"
            );


            //mapping
            author1.addBook(book1);
            author1.addBook(book2);
            author1.addBook(book3);

            author2.addBook(book4);
            author2.addBook(book5);
            author2.addBook(book6);


            Publisher pub1 = publisherDao.save(publisher1);
            pub1.addBook(book1);
            pub1.addBook(book2);
            pub1.addBook(book3);

            Publisher pub2 = publisherDao.save(publisher2);
            pub2.addBook(book4);
            pub2.addBook(book5);
            pub2.addBook(book6);

            Genre gen1 = genreDao.save(genre1);//manage state for resolve transient error
            Genre gen2 = genreDao.save(genre2);

            book1.addGenres(gen1);
            book2.addGenres(gen1);
            book3.addGenres(gen1);

            book4.addGenres(gen2);
            book5.addGenres(gen2);
            book6.addGenres(gen2);

            /*bookDao.save(book1);
            bookDao.save(book2);
            bookDao.save(book3);
            bookDao.save(book4);
            bookDao.save(book5);
            bookDao.save(book6);*/

            authorDao.save(author1);
            authorDao.save(author2);

        };
    }

    public static void main(String[] args) {
        SpringApplication.run(BookShopApplication.class, args);
    }

}
