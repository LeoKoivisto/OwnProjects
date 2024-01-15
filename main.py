import moderngl
import pygame as pg
import sys
from model import Cube
from camera import Camera
import numpy as np
import glm

class Graphics:
    def __init__(self, win_size=(800, 600)):
        pg.init()
        self.win_size = win_size
        self.screen = pg.display.set_mode(self.win_size, flags=pg.OPENGL | pg.DOUBLEBUF)
        self.ctx = moderngl.create_context()
        self.clock = pg.time.Clock()
        self.camera = Camera(self)
        self.cube = Cube(self.ctx, self.camera)
        self.left_mouse_button_pressed = False
        #mouse 
        pg.mouse.set_visible(True)
        pg.event.set_grab(True)

    def check_events(self):

        for event in pg.event.get():

            if event.type == pg.QUIT or (event.type == pg.KEYDOWN and event.key == pg.K_ESCAPE):
                self.cube.destroy()
                pg.quit()
                sys.exit()

            elif event.type == pg.KEYDOWN:
                if event.key == pg.K_LEFT:
                    self.camera.camera_position.x -= 0.1
                elif event.key == pg.K_RIGHT:
                    self.camera.camera_position.x += 0.1
                elif event.key == pg.K_UP:  
                    self.camera.camera_position.y += 0.1
                elif event.key == pg.K_DOWN:
                    self.camera.camera_position.y -= 0.1                 
                self.camera.update_view_matrix()

            elif event.type == pg.MOUSEBUTTONDOWN:
                if event.button == 1:  # Left mouse button
                    self.left_mouse_button_pressed = True
                elif event.button == 4:  # Mouse wheel up
                    self.camera.camera_position.z -= 0.1
                elif event.button == 5:  # Mouse wheel down
                    self.camera.camera_position.z += 0.1
                self.camera.update_view_matrix()

            elif event.type == pg.MOUSEBUTTONUP:
                if event.button == 1:  # Left mouse button
                    self.left_mouse_button_pressed = False

            elif event.type == pg.MOUSEMOTION:
                if self.left_mouse_button_pressed:
                    x, y = event.rel
                    self.camera.mouse_update(x, y)
        
    def render(self):

        self.ctx.clear(0.3, 0.2, 1.0, 1.0)
    
        #this is for the cube to move
        #time_passed = pg.time.get_ticks() / 1000.0 
        #cube_x = 0.5 * np.sin(time_passed)  
        #cube_y = 0.0  
        #cube_z = 0.0  
        #self.cube.model_matrix = glm.translate(self.cube.model_matrix, glm.vec3(cube_x, cube_y, cube_z))
      
        self.cube.model_matrix = glm.mat4(1.0)
        
    
        
        self.cube.on_init(self.camera.m_proj)  
        self.cube.render()
    
        pg.display.flip()

    def run(self):
        while True:
            self.check_events()
            self.render()
            self.clock.tick(60)

if __name__ == '__main__':
    app = Graphics()
    app.run()
