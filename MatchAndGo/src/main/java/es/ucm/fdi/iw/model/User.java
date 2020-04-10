package es.ucm.fdi.iw.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Entity
@NamedQueries({
	@NamedQuery(name="User.byUsername",
	query="SELECT u FROM User u "
			+ "WHERE u.username = :username AND u.enabled = true"),
	@NamedQuery(name="User.hasUsername",
	query="SELECT COUNT(u) "
			+ "FROM User u "
			+ "WHERE u.username = :username")
})

public class User {

	private static Logger log = LogManager.getLogger(User.class);	
	private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public enum Role{
		USER,ADMIN, MOD
	}	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String username;
	private String firstName;
	private String lastName;

	@Column(unique=true)
	private String email;

	@Column(nullable = false)
	private String password;
	private String birthDate;
	private String gender;
	private String roles;
	private String photo;
	
	private boolean enabled;
	
	@OneToMany(mappedBy="evaluated")
	private List<Evaluation> receivedEvaluation;

	@OneToMany(mappedBy="evaluator")
	private List<Evaluation> senderEvaluation;
	
	@OneToMany(mappedBy="sender")
	private List<Message> sentMessages;
	
	@OneToMany(mappedBy="receiver")
	private List<Message> receivedMessages;

	@ManyToMany
	private List<Tags> tags;
	
	@ManyToMany(mappedBy = "participants")
	private List<Event> joinedEvents;
	
	@OneToMany(mappedBy="creator")
	private List<Event> createdEvents;
	
	@Override
	public int hashCode() {
		return Long.hashCode(getId());
	}

	@Override
	public boolean equals(Object o) {
		return ((User)o).getId() == getId();
	}
	
	/**
	 * Checks whether this user has a given role.
	 * @param role to check
	 * @return true iff this user has that role.
	 */
	public boolean hasRole(Role role) {
		String roleName = role.name();
		return Arrays.stream(roles.split(","))
				.anyMatch(r -> r.equals(roleName));
	}
	
	/**
	 * Tests a raw (non-encoded) password against the stored one.
	 * @param rawPassword to test against
	 * @return true if encoding rawPassword with correct salt (from old password)
	 * matches old password. That is, true iff the password is correct  
	 */
	public boolean passwordMatches(String rawPassword) {
		return encoder.matches(rawPassword, this.password);
	}

	/**
	 * Encodes a password, so that it can be saved for future checking. Notice
	 * that encoding the same password multiple times will yield different
	 * encodings, since encodings contain a randomly-generated salt.
	 * @param rawPassword to encode
	 * @return the encoded password (typically a 60-character string)
	 * for example, a possible encoding of "test" is 
	 * $2y$12$XCKz0zjXAP6hsFyVc8MucOzx6ER6IsC1qo5zQbclxhddR1t6SfrHm
	 */
	public static String encodePassword(String rawPassword) {
		return encoder.encode(rawPassword);
	}	
	public List<Event> getJoinedEvents() {
		return joinedEvents;
	}


	public void setJoinedEvents(List<Event> joinedEvents) {
		this.joinedEvents = joinedEvents;
	}


	public List<Event> getCreatedEvents() {
		return createdEvents;
	}


	public void setCreatedEvents(List<Event> createdEvents) {
		this.createdEvents = createdEvents;
	}


	public User() {
		super();
	}
	
	
	public User(long id,String username, String firstName, String lastname, String email, String password, String birthate, String gender, String roles) {
		this.id = id;
		this.username= username;
		this.firstName = firstName;
		this.lastName = lastname;
		this.roles = roles;
		this.password = password;
		this.email = email;
		this.birthDate = birthate;
		this.gender = gender;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String nombre) {
		this.firstName = nombre;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastname) {
		this.lastName = lastname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String mail) {
		this.email = mail;
	}
	
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the password to an encoded value. 
	 * You can generate encoded passwords using {@link #encodePassword}.
	 * call only with encoded passwords - NEVER STORE PLAINTEXT PASSWORDS
	 * @param encodedPassword to set as user's password
	 */
	public void setPassword(String encodedPassword) {
		this.password = encodedPassword;
	}

	
	public String getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(String birthdate) {
		this.birthDate = birthdate;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setSexo(String sexo) {
		this.gender = sexo;
	}
	
	public String getRole() {
		return roles;
	}
	
	public void setRole(String roles) {
		this.roles = roles;
	}
	
	public String getPhoto() {
		return photo;
	}
	
	public void setPhoto(String img) {
		this.photo = img;
	}
	

	public List<Evaluation> getReceivedEvaluation() {
		return receivedEvaluation;
	}
	
	public void setReceivedEvaluation(List<Evaluation> evaluation) {
		this.receivedEvaluation = evaluation;
	}
	
	public List<Evaluation> getSenderEvaluation() {
		return senderEvaluation;
	}
	
	public void setSenderEvaluation(List<Evaluation> evaluation) {
		this.senderEvaluation = evaluation;
	}
	

	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}

	public List<Message> getSentMessages() {
		return sentMessages;
	}

	public void setSentMessages(List<Message> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}

	public void setReceivedMessages(List<Message> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	

}
