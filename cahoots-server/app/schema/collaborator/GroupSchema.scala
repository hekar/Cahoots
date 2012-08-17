package schema.collaborator

/**
 * The relationship between a user and a group
 */
class GroupUserModel(val group: String, val user: String, val collaborator: String) {
  
}


/**
 * The group is a set of collaborators
 */
class GroupModel(val guid: String, var name: String, collaborators: List[GroupUserModel]) {
	
}

object GroupModel {
}

object GroupCollaborators {
}