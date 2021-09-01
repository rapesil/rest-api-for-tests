import com.peixoto.api.domain.Book
import com.peixoto.api.repository.BookRepository
import com.peixoto.api.services.BookService
import spock.lang.Specification

class BookServiceTest extends Specification {
    BookService bookService
    BookRepository bookRepository

    def setup() {
        bookRepository = Stub(BookRepository.class)
        bookService = new BookService(bookRepository);

    }

    def 'should return two books' () {
        given: 'two books registered previously'
        bookRepository.findAll() >> [new Book(), new Book()]

        when: 'do a search'
        List<Book> list = bookService.findAll()

        then: 'a receive a list with two books'
        list.size() == 2
    }

    def 'should throw nullpointer exception' () {
        given: 'there is no books'
        bookRepository.findAll() >> null

        when: 'do a search'
        bookService.findAll()

        then: 'throw nullpointer exception'
        thrown(NullPointerException)
    }
}