import numpy as np
from webcolors import name_to_hex
import math
import random

def v2(x, y):
    return np.array([x, y], dtype=np.float64)
def v3(x, y, z):
    return np.array([x, y, z], dtype=np.float64)
def v4(*args): # (x, y, z, w) or (v3, w) or (v3)
    if len(args) == 4:
        return np.array([args[0], args[1], args[2], args[3]], dtype=np.float64)
    elif len(args) == 2:
        return np.array([args[0][0], args[0][1], args[0][2], args[1]], dtype=np.float64)
    elif len(args) == 1:
        return np.array([args[0][0], args[0][1], args[0][2], 1], dtype=np.float64)

def v2_zero():
    return v2(0,0)
def v2_right():
    return v2(1,0)
def v2_left():
    return v2(-1,0)
def v2_up():
    return v2(0,1)
def v2_down():
    return v2(0,-1)

def v3_zero():
    return v3(0,0,0)
def v3_right():
    return v3(1,0,0)
def v3_left():
    return v3(-1,0,0)
def v3_up():
    return v3(0,1,0)
def v3_down():
    return v3(0,-1,0)
def v3_forward():
    return v3(0,0,1)
def v3_backwards():
    return v3(0,0,-1)
def v3_from_v4(v4):
    return v3(v4[0], v4[1], v4[2])

def length(v):
    return np.linalg.norm(v)
def lengthSqr(v):
    return v @ v
def unit(v):
    return v / np.linalg.norm(v, keepdims=True)
def dot(v, w):
    return v @ w
def cross(v, w):
    return np.cross(v, w)
def vecEqual(a, b):
    return np.array_equal(a, b)

def angleDeg(v):
    return math.degrees(math.atan2(v[1],v[0]))

def randRange(min, max):
    return min + (max - min) * random.random()

def posAngleDeg(a):
    return a if a >= 0 else a + 360
def compareDst(a, b):
    return int(a * 100) == int(b * 100)
def compareAngle(a, b):
    return int(posAngleDeg(a) * 10) == int(posAngleDeg(b) * 10)

def minV2(a, b):
    return v2(min(a[0], b[0]), min(a[1], b[1]))
def maxV2(a, b):
    return v2(max(a[0], b[0]), max(a[1], b[1]))
def minV3(a, b):
    return v3(min(a[0], b[0]), min(a[1], b[1]), min(a[2], b[2]))
def maxV3(a, b):
    return v3(max(a[0], b[0]), max(a[1], b[1]), max(a[2], b[2]))
def clamp(value, minValue, maxValue):
    return max(min(value, maxValue), minValue)
def clampV2(value, minValue, maxValue):
    return maxV2(minV2(value, maxValue), minValue)
def clampV3(value, minValue, maxValue):
    return maxV3(minV3(value, maxValue), minValue)

def lerp(a, b, t):
    return a + (b - a) * clamp(t, 0, 1)
def easeInOut(t, factor = 4):
    if t < 0.5:
        return factor * t * t
    return 1 - factor * (1 - t) * (1 - t)

def sinDeg(angleDeg):
    return math.sin(math.radians(angleDeg))
def cosDeg(angleDeg):
    return math.cos(math.radians(angleDeg))
def tanDeg(angleDeg):
    return math.tan(math.radians(angleDeg))
def asinDeg(r):
    return math.degrees(math.asin(r))
def acosDeg(r):
    return math.degrees(math.acos(r))
def atanDeg(r):
    return math.degrees(math.atan(r))
def atan2Deg(y, x):
    return math.degrees(math.atan2(y, x))

def v2ToAngleDeg(v):
    vUnit = unit(v)
    return atan2Deg(v[1], v[0])

def toFloat(text, default = 0.0):
    try:
        return float(text)
    except ValueError:
        return default

def limitMag(v, maxMag):
    mag = np.linalg.norm(v)
    if mag > maxMag:
        return v * (maxMag / mag)
    return v

def clampToClosestHalfUnit(pt):
    return v2(round(pt[0] - 0.5) + 0.5, round(pt[1] - 0.5) + 0.5)

def minAngleToAngleDelta(a1, a2):
    d1 = a2 - a1
    d2 = a2 - (a1 + 360)
    d3 = a2 - (a1 - 360)
    if abs(d1) < abs(d2):
        return d1 if (abs(d1) < abs(d3)) else d3
    return d2 if (abs(d2) < abs(d3)) else d3

def rotateVec2(v, angleDeg):
    angle = math.radians(angleDeg)
    cos = math.cos(angle)
    sin = math.sin(angle)
    return v2(v[0] * cos - v[1] * sin, v[0] * sin + v[1] * cos)

def colorNamedToHex(colorNamed):
    return name_to_hex(colorNamed) if not colorNamed.startswith("#") else colorNamed
def colorHexToRgb(colorHex):
    colorHex = colorHex.lstrip('#')
    return tuple(int(colorHex[i:i+2], 16) for i in (0, 2, 4))
def colorRgbToHex(colorRGB):
    return "#%02x%02x%02x" % colorRGB
def colorRgbToHex(r, g, b):
    return ("#%02x" % r) + ("%02x" % g) + ("%02x" % b)
def colorHexLerp(color1, color2, t):
    r1, g1, b1 = colorHexToRgb(color1)
    r2, g2, b2 = colorHexToRgb(color2)
    r = int(r1 + (r2 - r1) * t)
    g = int(g1 + (g2 - g1) * t)
    b = int(b1 + (b2 - b1) * t)
    return colorRgbToHex(r, g, b)

def appendElements(toBeAppendedTo, elementsToAppend):
    for el in elementsToAppend:
        toBeAppendedTo.append(el)
    return toBeAppendedTo

# intersection/Distances (returns null if no intersection, vec2 with intersection otherwise)...
def distancePointToLine(point, linePoint, lineDirUnit):
    v = point - linePoint
    u = v - lineDirUnit * dot(v, lineDirUnit)
    return u.length()
def distancePointToPlane(point, linePoint, lineNormalUnit):
    v = point - linePoint
    return dot(v, lineNormalUnit)
def distanceAlongRayToPlane(planePoint, planeNormal, rayOrigin, rayDirUnit):
    n_dot_d = dot(planeNormal, rayDirUnit)
    if abs(n_dot_d) < 1e-6:
        return 0 # ray and plane are parallel
    t = dot(planeNormal, planePoint - rayOrigin) / n_dot_d
    if t < 0:
        return 0 # intersection point is behind the ray origin
    return t
def intersectSegments(seg0, seg0dir, seg1, seg1dir):
    divisor = (-seg1dir[0] * seg0dir[1] + seg0dir[0] * seg1dir[1])
    divisor = 0.0000001 if divisor == 0 else divisor
    s = (-seg0dir[1] * (seg0[0] - seg1[0]) + seg0dir[0] * (seg0[1] - seg1[1])) / divisor
    t = ( seg1dir[0] * (seg0[1] - seg1[1]) - seg1dir[1] * (seg0[0] - seg1[0])) / divisor
    if (s >= 0 and s <= 1 and t >= 0 and t <= 1):
        # Collision detected
        return v2(seg0[0] + (t * seg0dir[0]), seg0[1] + (t * seg0dir[1]))
    return None
def intersectCircles(p1, r1, p2, r2):
    dstSqr = lengthSqr(p1 - p2)
    radSum = r1 + r2
    return dstSqr < radSum * radSum
def intersectCirclePt(circlePos, circleRad, point):
    dstSqr = lengthSqr(circlePos - point)
    return dstSqr < circleRad * circleRad
def intersectRectCircle(circlePt, circleRadius, rcCenter, rcHalfDims, rcAngleDeg):
    rcRight = unit(rotateVec2( v2_right(), rcAngleDeg ))
    rcUp = unit(rotateVec2( v2_up(), rcAngleDeg ))
    delta = circlePt - rcCenter
    dst = v2( abs(dot(rcRight, delta)), abs(dot(rcUp, delta)) )
    return (dst[0] - circleRadius) <= rcHalfDims[0] and (dst[1] - circleRadius) <= rcHalfDims[1]
def intersectSphereSegment(sphereCenter, sphereRadius, segStart, segDir):
    v = sphereCenter - segStart
    segLen = length(segDir)
    segDirUnit = segDir * (1 / segLen)
    alongSegT = min(max((dot(v, segDirUnit) / segLen), 0), 1)
    closestPoint = segStart + segDir * alongSegT
    intersectsSphere = (sphereCenter.distanceSqr(closestPoint) < (sphereRadius * sphereRadius))
    return { 'intersects':intersectsSphere, 'closestPoint':closestPoint, 'closestPointT':alongSegT }
def intersectLinePlane(planePoint, planeNormal, linePoint, lineDirUnit):
    if dot(planeNormal, lineDirUnit) == 0:
        return None
    t = (dot(planeNormal, planePoint) - dot(planeNormal, linePoint)) / dot(planeNormal, lineDirUnit)
    return linePoint + lineDirUnit * t
def isInsideField(window, pt, buffer = 0):
    if pt[0] <= buffer or pt[1] <= buffer:
        return False
    if pt[0] >= window.maxCoordinateX() - buffer or pt[1] >= window.maxCoordinateY() - buffer:
        return False
    return True

def calcRotatedRectanglePtsInPixels(center, rcHalfDims, rcOffset, angleDeg, window):
    ul = window.toPixels(center + rotateVec2(v2(-rcHalfDims[0], rcHalfDims[1]) + rcOffset, angleDeg))
    ur = window.toPixels(center + rotateVec2(v2( rcHalfDims[0], rcHalfDims[1]) + rcOffset, angleDeg))
    lr = window.toPixels(center + rotateVec2(v2( rcHalfDims[0],-rcHalfDims[1]) + rcOffset, angleDeg))
    ll = window.toPixels(center + rotateVec2(v2(-rcHalfDims[0],-rcHalfDims[1]) + rcOffset, angleDeg))
    return [ (ul[0], ul[1]), (ur[0], ur[1]), (lr[0], lr[1]), (ll[0], ll[1]) ]
def calcRotatedTrianglePtsInPixels(center, minX, maxX, minY, maxY, angle, window): # angle of zero is right facing
    local = [ v2(minX, maxY), v2(maxX, 0), v2(minX, minY) ]
    pixel = []
    for i in range(len(local)):
        pixel.append( window.toPixels(center + rotateVec2(local[i], angle)) )
    return [ (pixel[0][0], pixel[0][1]), (pixel[1][0], pixel[1][1]), (pixel[2][0], pixel[2][1]) ]

def m3x3Identity():
    return np.eye(3)
def m3x3Transpose(m3x3):
    return m3x3.T
def m3x3Inverse(m3x3):
    return np.linalg.inv(m3x3)
def m3x3RotX(angleDeg):
	return np.array([[1, 0, 0], 
                     [0, math.cos(math.radians(angleDeg)), -math.sin(math.radians(angleDeg))], 
                     [0, math.sin(math.radians(angleDeg)), math.cos(math.radians(angleDeg))]])
def m3x3RotY(angleDeg):
	return np.array([[math.cos(math.radians(angleDeg)), 0, math.sin(math.radians(angleDeg))], 
                     [0, 1, 0], 
                     [-math.sin(math.radians(angleDeg)), 0, math.cos(math.radians(angleDeg))]])
def m3x3RotZ(angleDeg):
	return np.array([[math.cos(math.radians(angleDeg)), -math.sin(math.radians(angleDeg)), 0], 
                     [math.sin(math.radians(angleDeg)), math.cos(math.radians(angleDeg)), 0], 
                     [0, 0, 1]])
def m3x3RotAxis(axis, angleDeg): # rotation about specified axis
	axis = unit(axis)
	return np.array([[math.cos(math.radians(angleDeg)) + axis[0] * axis[0] * (1 - math.cos(math.radians(angleDeg))), axis[0] * axis[1] * (1 - math.cos(math.radians(angleDeg))) - axis[2] * math.sin(math.radians(angleDeg)), axis[0] * axis[2] * (1 - math.cos(math.radians(angleDeg))) + axis[1] * math.sin(math.radians(angleDeg))],
	                 [axis[1] * axis[2] * (1 - math.cos(math.radians(angleDeg))) + axis[2] * math.sin(math.radians(angleDeg)), math.cos(math.radians(angleDeg)) + axis[1] * axis[1] * (1 - math.cos(math.radians(angleDeg))), axis[1] * axis[2] * (1 - math.cos(math.radians(angleDeg))) - axis[0] * math.sin(math.radians(angleDeg))],
					 [axis[2] * axis[0] * (1 - math.cos(math.radians(angleDeg))) - axis[1] * math.sin(math.radians(angleDeg)), axis[2] * axis[1] * (1 - math.cos(math.radians(angleDeg))) + axis[0] * math.sin(math.radians(angleDeg)), math.cos(math.radians(angleDeg)) + axis[2] * axis[2] * (1 - math.cos(math.radians(angleDeg)))]])
def m3x3LookAt(forward, up = v3_up()):
    forward = unit(forward)
    right = unit(cross(up, forward))
    up = np.cross(forward, right)
    return np.array([[right[0], right[1], right[2]], 
                     [up[0], up[1], up[2]], 
                     [forward[0], forward[1], forward[2]]])
def m3x3Diag(diag):
    return np.diag(diag)
def m3x3OrthoNormalize(m3x3):
    # QR Decomp (rather than Gram-Schmidt)
    q, r = np.linalg.qr(m3x3)
    # Can invert an axis, so deal with that
    det_q = np.linalg.det(q)
    if det_q < 0:
        q[:, 2] *= -1  # Invert the z-axis
    return q
    
def m4x4Identity():
    return np.eye(4)
def m4x4Transpose(m4x4):
    return m4x4.T
def m4x4Inverse(m4x4):
    return np.linalg.inv(m4x4)
def m4x4RotX(angleDeg):
    m4x4 = np.zeros((4, 4))
    m4x4[3, 3] = 1
    m4x4[:3, :3] = m3x3RotX(angleDeg)
    return m4x4
def m4x4RotY(angleDeg):
    m4x4 = np.zeros((4, 4))
    m4x4[3, 3] = 1
    m4x4[:3, :3] = m3x3RotY(angleDeg)
    return m4x4
def m4x4RotZ(angleDeg):
    m4x4 = np.zeros((4, 4))
    m4x4[3, 3] = 1
    m4x4[:3, :3] = m3x3RotZ(angleDeg)
    return m4x4
def m4x4RotAxis(axis, angleDeg):
    m4x4 = np.zeros((4, 4))
    m4x4[3, 3] = 1
    m4x4[:3, :3] = m3x3RotAxis(axis, angleDeg)
    return m4x4
def m4x4Trans(offset):
    m4x4 = np.zeros((4, 4))
    for i in range(4):
        m4x4[i, i] = 1
    for i in range(3):
        m4x4[i, 3] = offset[i]
    return m4x4
def m4x4ModelToWorld(orient, pos):
    modelToWorld = np.eye(4)
    modelToWorld[:3, :3] = orient
    modelToWorld[:3, 3] = pos
    return modelToWorld
def m4x4LookAt(forward, up = v3_up(), pos = v3_zero()):
    m4x4 = np.zeros((4, 4))
    m4x4[:3, :3] = m3x3LookAt(forward, up)
    for i in range(3):
        m4x4[i, 3] = pos[i]
    m4x4[3, 3] = 1
    return m4x4
def m4x4Proj(fovVertFullDeg = 90, clipNear = 0.1, clipFar = 1000, aspectWoverH = 1):
    fovS = 1 / math.tan( math.radians(fovVertFullDeg) / 2 )
    m4x4 = np.zeros((4, 4))
    m4x4[0,0] = fovS / aspectWoverH
    m4x4[1,1] = fovS
    m4x4[2,2] = (clipFar + clipNear) / (clipNear - clipFar)
    m4x4[2,3] = -(2 * clipFar * clipNear) / (clipNear - clipFar)
    m4x4[3,2] = -1
    return m4x4

def log(str):
    print(str)