package co.develhope.TestCrud02.controller;

import co.develhope.TestCrud02.entities.User;
import co.develhope.TestCrud02.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok().body(userRepository.save(user));
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAll(){
        return userRepository.findAll().isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(userRepository.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getSingle(@PathVariable Long id) {
        return userRepository.findById(id).isPresent() ? ResponseEntity.ok().body(userRepository.findById(id).get()) :
                ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.get().setSurname(user.getSurname());
        optionalUser.get().setEmail(user.getEmail());
        return ResponseEntity.of(optionalUser);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable Long id){
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
