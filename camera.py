import glm

FOV = 140
NEAR = 0.1
FAR = 10.0

class Camera:
    def __init__(self, app):
        self.app = app
        self.aspect_ratio = app.win_size[0] / app.win_size[1]
        self.m_proj = self.get_projection_matrix()

      
        self.camera_position = glm.vec3(0.0, 0.0, 2.0)  
        self.camera_target = glm.vec3(0.0, 0.0, 0.0)  
        self.camera_up = glm.vec3(0.0, 1.0, 0.0)  
       
        self.m_view = glm.lookAt(self.camera_position, self.camera_target, self.camera_up)

  

    def get_projection_matrix(self):
        return glm.perspective(glm.radians(FOV), self.aspect_ratio, NEAR, FAR)

    def get_view_matrix(self):
        return self.m_view
    
    def mouse_update(self, dx, dy):

        sensitivity = 0.01
        dx *= sensitivity
        dy *= sensitivity

        self.camera_target.x += dx
        self.camera_target.y += dy
        self.update_view_matrix()

    def update_view_matrix(self):
        self.m_view = glm.lookAt(self.camera_position, self.camera_target, self.camera_up)