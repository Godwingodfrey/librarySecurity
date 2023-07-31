package com.librarySecurity.book;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final IBookService iBookService;

    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks(){
        return new ResponseEntity<>(iBookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}")
    public Optional<Book> getById(@PathVariable Long bookId){
        return iBookService.findBookById(bookId);
    }

    @PostMapping("/add")
    public ResponseEntity<Book> add(@RequestBody Book book){
        return new ResponseEntity<>(iBookService.add(book), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Book> update(@RequestBody Book theBook){
        return new ResponseEntity<>(iBookService.update(theBook), HttpStatus.OK);
    }

    @DeleteMapping("/book/delete/{id}")
    public void delete(@PathVariable("id") Long bookId){
        iBookService.delete(bookId);
    }
}
