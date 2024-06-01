package com.vn.projectmanagement.security.JWT;

import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class JwtUserDetail implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Tìm người dùng theo username
     *
     * @param username usernam người dùng cần tìm
     * @return UserDetails của người dùng
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm người dùng theo username
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        // Trả về UserDetails của người dùng
        // UserDetails chứa thông tin về người dùng như username, password, quyền. UserDetails sẽ được sử dụng trong quá trình xác thực và phân quyền
        // Nếu không có UserDetails, Spring Security sẽ không thể xác thực và phân quyền cho người dùng và sẽ không thể truy cập vào các API
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                this.mapRolesToAuthorities(user.getRole())
        );
    }

    /**
     * Map role của người dùng thành các quyền
     *
     * @param roleName role của người dùng
     * @return danh sách các quyền của người dùng
     */
    private Collection<GrantedAuthority> mapRolesToAuthorities(final String roleName) {
        // Tạo danh sách quyền. Mỗi quyền sẽ được thêm vào danh sách dưới dạng SimpleGrantedAuthority
        // Ví dụ: authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // Với ROLE_USER là quyền của người dùng. Nếu người dùng có nhiều quyền, thì thêm nhiều quyền vào danh sách
        // Ví dụ: authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // Thêm quyền của người dùng vào danh sách
        authorities.add(new SimpleGrantedAuthority(roleName));
        return authorities;
    }
}
