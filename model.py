import numpy as np
import moderngl
import glm

class Triangle:
    def __init__(self, ctx):
        self.ctx = ctx
        self.vbo = self.create_vbo()
        self.shader = self.create_shader()
        self.vao = self.get_vao()

    def get_vao(self):
        vao = self.ctx.vertex_array(self.shader, [(self.vbo, '3f', 'in_vert')])
        return vao

    def render(self):
        self.vao.render()

    def destroy(self):
        self.vao.release()
        self.vbo.release()
        self.shader.release()
        
    def get_data(self):
        vertex_data = [(-0.6, -0.8, 0.0), (0.6, -0.8, 0.0), (0.0, 0.8, 0.0)]
        vertex_data = np.array(vertex_data, dtype='f4')
        return vertex_data
    
    def create_vbo(self):
        vertex_data = self.get_data()
        vbo = self.ctx.buffer(vertex_data.tobytes())
        return vbo
    
    def create_shader(self):
        vertex_shader = """
            #version 330
            in vec3 in_vert;
            void main() {
                gl_Position = vec4(in_vert, 1.0);
            }
        """
        fragment_shader = """
            #version 330
            out vec4 f_color;
            void main() {
                f_color = vec4(0.3, 0.5, 1.0, 1.0);
            }
        """
        shader = self.ctx.program(vertex_shader=vertex_shader, fragment_shader=fragment_shader)
        return shader




class Cube:
    
    def __init__(self, ctx, camera):
        self.ctx = ctx
        self.vbo = self.create_buffers()
        self.shader = self.create_shader()
        self.vao = self.get_vao()
        self.camera = camera

       
        self.color = (1.0, 0.0, 0.0, 1.0)  
        self.model_matrix = glm.mat4(1.0)  
        self.scale = 0.4  

    def render(self):
        self.shader["m_proj"].write(self.camera.m_proj)
        self.shader["m_view"].write(self.camera.get_view_matrix())
        self.on_init(self.camera.m_proj)  
        self.ctx.enable(moderngl.DEPTH_TEST)
        
        self.model_matrix = glm.scale(self.model_matrix, glm.vec3(self.scale, self.scale, self.scale))
        
        self.shader["m_model"].write(self.model_matrix)
        self.vao.render(moderngl.TRIANGLES)

 

    def on_init(self, projection_matrix): 
        self.shader["m_proj"].write(projection_matrix)


    def get_vao(self):
        vao = self.ctx.simple_vertex_array(self.shader, self.vbo, 'in_vert')
        return vao

    def destroy(self):
        self.vao.release()
        self.vbo.release()
        self.ebo.release()
        self.shader.release()

    def create_buffers(self):
        vertex_data = self.get_vertex_data()
        vbo = self.ctx.buffer(vertex_data.tobytes())
        
        return vbo

    def get_vertex_data(self):

        vertices = np.array([  
            -0.5, -0.5, -0.5,  # Front bottom-left
             0.5, -0.5, -0.5,  # Front bottom-right
             0.5,  0.5, -0.5,  # Front top-right
            -0.5,  0.5, -0.5,  # Front top-left
            -0.5, -0.5,  0.5,  # Back bottom-left
             0.5, -0.5,  0.5,  # Back bottom-right
             0.5,  0.5,  0.5,  # Back top-right
            -0.5,  0.5,  0.5,  # Back top-left
        ], dtype='f4')

        indices = np.array([
            # Front face
            0, 1, 2, # First triangle
            2, 3, 0, # Second triangle
            
            # Back face
            4, 5, 6,
            6, 7, 4,
            
            # Top face
            3, 2, 6,
            6, 7, 3,
            
            # Bottom face
            0, 1, 5,
            5, 4, 0,
            
            # Left face
            0, 3, 7,
            7, 4, 0,
            
            # Right face
            1, 5, 6,
            6, 2, 1,
        ], dtype='i4')

        vertex_data = self.get_data(vertices, indices)
        
        return vertex_data
    

    def get_data(self, vertices, indices):
        
        data = []
        for triangle in indices.reshape(-1, 3):
            for ind in triangle:
                vertex = vertices[ind * 3 : ind * 3 + 3]  
                data.extend(vertex)

        return np.array(data, dtype='f4')
    

    def create_shader(self):
        vertex_shader = """
            #version 330
            in vec3 in_vert;
            uniform mat4 m_proj;
            uniform mat4 m_model;
            uniform mat4 m_view;  // Add view matrix uniform
            void main() {
                gl_Position = m_proj * m_view * m_model * vec4(in_vert, 1.0); 
            }
"""
        fragment_shader = f"""
            #version 330
            out vec4 f_color;
            void main() {{
                f_color = vec4(1.0, 0.0, 0.0, 1.0); // Red color
            }}
        """
        shader = self.ctx.program(vertex_shader=vertex_shader, fragment_shader=fragment_shader)
        return shader