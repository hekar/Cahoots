package com.cahoot.client.services
import com.cahoot.models.DocumentModel
import com.cahoot.models.PersonModel
import com.cahoot.models.ShareModel
import com.google.inject.Inject
import com.cahoot.client.connection.Connection
import com.cahoot.client.connection.ConnectionComponent


/**
 * Service to 
 */
class CollabService extends ConnectionComponent {
 
  def share(doc: DocumentModel, 
      collaborators: List[PersonModel]): ShareModel = {
    
    // TODO: Setup the sharing of this document
    
    new ShareModel(doc, collaborators)
  }
  
  
  def listCollaborators(receiver: (List[PersonModel]) => Unit):Unit  = {
    new Thread(new Runnable() {
		def run():Unit = {
			connection.write()
			connection.read()
		}
	})
  }
  
}
