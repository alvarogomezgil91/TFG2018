class Valores_stock:
    def __init__(self, stock, fecha, apertura, maximo, minimo, cierre, adj_cierre, volume):
        self.stock = stock
        self.fecha = fecha
        self.apertura = apertura
        self.maximo = maximo
        self.minimo = minimo
        self.cierre = cierre
        self.adj_cierre = adj_cierre
        self.volume = volume
        #self.tendencia = self.obtiene_tendencia(apertura, cierre)

    def set_stock(self, stock):
        self.stock = stock
    def get_stock(self):
        return self.stock

    def set_fecha(self, fecha):
        self.fecha = fecha
    def get_fecha(self):
        return self.fecha

    def set_apertura(self, apertura):
        self.apertura = apertura
    def get_apertura(self):
        return self.apertura

    def set_maximo(self, maximo):
        self.maximo = maximo
    def get_maximo(self):
        return self.maximo

    def set_minimo(self, minimo):
        self.minimo = minimo
    def get_minimo(self):
        return self.minimo

    def set_cierre(self, cierre):
        self.cierre = cierre
    def get_cierre(self):
        return self.cierre

    def set_adj_cierre(self, adj_cierre):
        self.adj_cierre = adj_cierre
    def get_adj_cierre(self):
        return self.adj_cierre

    def set_volume(self, volume):
        self.volume = volume
    def get_volume(self):
        return self.volume

    def set_tendencia(self, tendencia):
        self.tendencia = tendencia
    def get_tendencia(self):
        return self.tendencia

    def obtiene_tendencia(apertura, cierre):

        if(apertura > cierre):
            tendencia = 1001
        elif(apertura < cierre):
            tendencia = 1002
        else:
            tendencia = 1003

        return tendencia

